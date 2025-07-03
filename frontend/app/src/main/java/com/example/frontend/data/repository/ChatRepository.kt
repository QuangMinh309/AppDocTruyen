package com.example.frontend.data.repository
import android.util.Log
import com.example.frontend.data.model.Chat
import com.example.frontend.services.websocket.WebSocketManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

// WebSocketMessage tổng quát
data class WebSocketMessage(
    val success: Boolean? = null,
    val action: String? = null,
    val payload: Any? = null,
    val type: String? = null, // Cho lỗi từ middleware
    val statusCode: Int? = null, // Cho lỗi từ middleware
    val message: String? = null // Cho lỗi từ middleware
)



// Repository để quản lý kết nối WebSocket và dữ liệu chat
@Singleton
class ChatRepository @Inject constructor(
    private val webSocketManager:WebSocketManager,
    private val gson: Gson,
) {
    private val roomName = "chat"

    private val _chatList = MutableStateFlow<List<Chat>>(emptyList())
    val chatList : StateFlow<List<Chat>> = _chatList
    private val _isConnected = MutableStateFlow(false)
    val isConnected  : StateFlow<Boolean> = _isConnected

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("ChatRepository", "WebSocket opened!")
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
             handleWebSocketMessage(text)
        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response:Response?) {
            Log.e("WebSocket", "Error: ${t.message}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.w("ChatRepository", "WebSocket closing: $reason, code: $code")
            webSocket.close(1000, null)
        }
    }

    suspend fun connect(communityId: Int) {
        webSocketManager.addListener(roomName,listener)
        webSocketManager.connect(  room = roomName, bonusQueryString = "community=$communityId" )
        Log.d("ChatRepository", "Connecting to communityId: $communityId")
    }

    private suspend fun reconnect(communityId: Int) {
       // Logic reconnect (tùy thuộc vào WebSocketManager)
        delay(2000) // Delay trước khi reconnect
        connect(communityId)
    }

    // Xử lý tin nhắn từ WebSocket
    private fun handleWebSocketMessage(message: String) {
        scope.launch {
            try {
                val wsMessage = gson.fromJson(message, WebSocketMessage::class.java)
                if (wsMessage.type == "ERROR") {
                    Log.e("ChatRepository", "Server error: ${wsMessage.message}, code: ${wsMessage.statusCode}")
                    return@launch
                }
                if(wsMessage.type == "CONNECTION_SUCCESS" ){
                    scope.launch(Dispatchers.Main) {
                        _isConnected.emit(true)
                    }
                    Log.i("ChatRepository", "Connection success: ${wsMessage.message}")
                    return@launch
                }
                when (wsMessage.action) {
                    "FETCH_CHAT_BY_COMMUNITY" -> {
                        if (wsMessage.success == true) {
                            val chatMessages = gson.fromJson(
                                gson.toJson(wsMessage.payload),
                                Array<Chat>::class.java
                            ).toList()


                            scope.launch(Dispatchers.Main) {
                                _chatList .emit(chatMessages )
                            }
                        }
                    }
                    "BRC_CREATE_CHAT"-> {
                        val chatMessage = gson.fromJson(gson.toJson(wsMessage.payload), Chat::class.java)
                        scope.launch(Dispatchers.Main) {

                            _chatList .emit(_chatList .value + chatMessage)
                        }
                    }
                    "BRC_UPDATE_CHAT" -> {
                        val chatMessage = gson.fromJson(gson.toJson(wsMessage.payload), Chat::class.java)

                        scope.launch(Dispatchers.Main) {
                           _chatList .emit(_chatList.value.map {chat->
                                if(chat.id == chatMessage.id) chatMessage else chat })
                        }
                    }
                    "BRC_DELETE_CHAT" -> {
                        val deletedChat = gson.fromJson(gson.toJson(wsMessage.payload), Chat::class.java)
                        scope.launch(Dispatchers.Main) {
                            _chatList.emit(_chatList .value.filter { it.id != deletedChat.id })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatRepository", "Error parsing message: ${e.message}")
            }
        }
    }

    // Gửi tin nhắn tạo chat
    fun createChat(content: String?, commentPicData: String?) {
        var newCommentPicData = commentPicData
        if(commentPicData?.isEmpty()==true) newCommentPicData = null
        val payload = mapOf(
            "content" to content,
            "commentPicData" to newCommentPicData
        )
        sendMessage("CREATE_CHAT", payload)
    }

    // Gửi tin nhắn cập nhật chat
    fun updateChat(chatId: String, content: String?, commentPicData: String?) {
        var newCommentPicData = commentPicData
        if(commentPicData?.isEmpty()==true) newCommentPicData = null
        val payload = mapOf(
            "chatId" to chatId,
            "content" to content,
            "commentPicId" to newCommentPicData
        )
        sendMessage("UPDATE_CHAT", payload)
    }

    // Gửi tin nhắn xóa chat
    fun deleteChat(chatId: Int) {
        val payload = mapOf("chatId" to chatId)
        sendMessage("DELETE_CHAT", payload)
    }

    // Gửi yêu cầu lấy danh sách chat
    fun  fetchChats() {
        val payload = {}
        sendMessage("FETCH_CHAT_BY_COMMUNITY", payload)
    }

    // Gửi tin nhắn tới server
    private fun sendMessage(action: String, payload: Any) {
        val message = WebSocketMessage(action = action, payload = payload)
        val json = gson.toJson(message)
        webSocketManager.sendMessage(room = roomName, message = json)
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {

        scope.launch(Dispatchers.Main) {
            webSocketManager.disconnect(room = roomName)
            _isConnected.emit(false)
        }
        _chatList.value = emptyList()
    }
}