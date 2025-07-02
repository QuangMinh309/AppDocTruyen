package com.example.frontend.data.repository
import android.util.Log
import com.example.frontend.data.model.Comment
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




// Repository để quản lý kết nối WebSocket và dữ liệu chat
@Singleton
class CommentRepository @Inject constructor(
    private val webSocketManager:WebSocketManager,
    private val gson: Gson,
) {
    private val roomName = "comment"

    private val _commentList = MutableStateFlow<List<Comment>>(emptyList())
    val commentList : StateFlow<List<Comment>> = _commentList
    private val _isConnected = MutableStateFlow(false)
    val isConnected  : StateFlow<Boolean> = _isConnected

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("CommentRepository", "WebSocket opened!")
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

    init {
        // Gán listener cho WebSocketManager
        webSocketManager. addListener(room = roomName,listener=listener)
    }

    suspend fun connect(communityId: Int) {
        webSocketManager.connect(  room = roomName, bonusQueryString = "community=$communityId" )
        Log.d("ChattingViewModel", "Connecting to communityId: $communityId")
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
                    Log.e("CommentRepository", "Server error: ${wsMessage.message}, code: ${wsMessage.statusCode}")
                    return@launch
                }
                if(wsMessage.type == "CONNECTION_SUCCESS" ){
                    scope.launch(Dispatchers.Main) {
                        _isConnected.emit(true)
                    }
                    Log.i("CommentRepository", "Connection success: ${wsMessage.message}")
                    return@launch
                }
                when (wsMessage.action) {
                    "FETCH_COMMENT_BY_CHAPTER" -> {
                        if (wsMessage.success == true) {
                            val chatMessages = gson.fromJson(
                                gson.toJson(wsMessage.payload),
                                Array<Comment>::class.java
                            ).toList()


                            scope.launch(Dispatchers.Main) {
                                _commentList.emit(chatMessages )
                            }
                        }
                    }
                    "BRC_CREATE_COMMENT"-> {
                        val chatMessage = gson.fromJson(gson.toJson(wsMessage.payload), Comment::class.java)
                        scope.launch(Dispatchers.Main) {

                            _commentList .emit(_commentList .value + chatMessage)
                        }
                    }
                    "BRC_UPDATE_COMMENT" -> {
                        val chatMessage = gson.fromJson(gson.toJson(wsMessage.payload), Comment::class.java)

                        scope.launch(Dispatchers.Main) {
                            _commentList .emit(_commentList.value.map {chat->
                                if(chat.id == chatMessage.id) chatMessage else chat })
                        }
                    }
                    "BRC_DELETE_COMMENT" -> {
                        val deletedChat = gson.fromJson(gson.toJson(wsMessage.payload), Comment::class.java)
                        scope.launch(Dispatchers.Main) {
                            _commentList.emit(_commentList .value.filter { it.id != deletedChat.id })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("CommentRepository", "Error parsing message: ${e.message}")
            }
        }
    }

    // Gửi tin nhắn tạo chat
    fun createComment(content: String?, commentPicData: String?) {
        var newCommentPicData = commentPicData
        if(commentPicData?.isEmpty()==true) newCommentPicData = null
        val payload = mapOf(
            "content" to content,
            "commentPicData" to newCommentPicData
        )
        sendMessage("CREATE_COMMENT", payload)
    }


    // Gửi tin nhắn xóa comment
    fun deleteComment(chatId: Int) {
        val payload = mapOf("chatId" to chatId)
        sendMessage("DELETE_COMMENT", payload)
    }

    // Gửi yêu cầu lấy danh sách comment
    fun  fetchComments() {
        val payload = {}
        sendMessage("FETCH_COMMENT_BY_CHAPTER", payload)
    }

    // Gửi tin nhắn tới server
    private fun sendMessage(action: String, payload: Any) {
        val message = WebSocketMessage(action = action, payload = payload)
        val json = gson.toJson(message)
        webSocketManager.sendMessage(room = roomName, message = json)
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {
        webSocketManager.disconnect(room = roomName)
        _isConnected.value = false
        _commentList.value = emptyList()
        webSocketManager.removeListener(room = roomName, listener = listener)

    }
}