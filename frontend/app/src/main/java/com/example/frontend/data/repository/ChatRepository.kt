
import android.util.Log
import com.example.frontend.R
import com.example.frontend.data.local.dao.ChatDao
import com.example.frontend.data.local.entities.ChatEntity
import com.example.frontend.data.model.Chat
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.ImageUrlProvider
import com.example.frontend.services.websocket.WebSocketManager
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.time.LocalDateTime
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

// ChatMessage để parse payload
data class ChatMessage(
    @SerializedName("chatId") val chatId: Int,
    val communityId: Int,
    val content: String?,
    @SerializedName("commentPicId") val commentPicId: String?,
    val time: String?,
    val sender: User
)

// Repository để quản lý kết nối WebSocket và dữ liệu chat
@Singleton
class ChatRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val webSocketManager:WebSocketManager,
    private val imageUrlProvider: ImageUrlProvider,
    private val gson: Gson,
    private val dao: ChatDao
) {
    val roomName = "Chat"
    private val _chatState = MutableStateFlow<List<Chat>>(emptyList())
    val chatState: StateFlow<List<Chat>> = _chatState
    private val scope = CoroutineScope(Dispatchers.IO)
    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("ChatRepository", "WebSocket opened!")
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            handleWebSocketMessage(text)
        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            println("WebSocket error: ${t.message}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            println("WebSocket closing: $reason")
        }
    }

    init {
        // Gán listener cho WebSocketManager
        webSocketManager. addListener(room = roomName,listener=listener)
    }
    suspend fun connect(communityId: String) = webSocketManager.connect(  room = roomName, bonusQueryString = "community=$communityId" )


    // Xử lý tin nhắn từ WebSocket
    private fun handleWebSocketMessage(message: String) {
        scope.launch {
            try {
                val wsMessage = gson.fromJson(message, WebSocketMessage::class.java)
                if (wsMessage.type == "ERROR") {
                    Log.e("ChatRepository", "Server error: ${wsMessage.message}, code: ${wsMessage.statusCode}")
                    return@launch
                }
                when (wsMessage.action) {
                    "CONNECTION_SUCCESS" -> {
                        println("Connection success: ${wsMessage.payload}")
                    }

                    "FETCH_CHAT_BY_COMMUNITY" -> {
                        if (wsMessage.success == true) {
                            val chatMessages = gson.fromJson(
                                gson.toJson(wsMessage.payload),
                                Array<ChatMessage>::class.java
                            ).toList()
                            val chats = chatMessages.map { chatMessage ->
                                var sender = User(
                                    id = chatMessage.sender.id,
                                    dName = chatMessage.sender.name ,
                               //     avatarUrl = chatMessage.sender.avatarUrl
                                )
                                Chat(
                                    id = chatMessage.chatId,
                                    communityId = chatMessage.communityId,
                                    sender = chatMessage.sender,
                                    content = chatMessage.content,
                                    messagePicUrl = imageUrlProvider.fetchImage(chatMessage.commentPicId ?: ""),
                                    time = chatMessage.time?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
                                )
                            }

                            // Lưu vào Room
                            val entities = chats.map { chat ->
                                ChatEntity(
                                    chatId = chat.id,
                                    communityId = chat.communityId,
                                    senderId = chat.sender.id,
                                    content = chat.content,
                                    messagePicUrl = chat.messagePicUrl,
                                    time = chat.time.toString(),
                                    senderName = chat.sender.name,
                                    senderAvatarUrl = chat.sender.avatarUrl
                                )
                            }
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insertAll(entities)
                                _chatState.emit(chats)
                            }
                        }
                    }
                    "BRC_CREATE_CHAT", "BRC_UPDATE_CHAT" -> {
                        val newChat = gson.fromJson(gson.toJson(wsMessage.payload), Chat::class.java)
                        _chatState.emit(_chatState.value + newChat)
                    }
                    "BRC_DELETE_CHAT" -> {
                        val deletedChat = gson.fromJson(gson.toJson(wsMessage.payload), Chat::class.java)
                        _chatState.emit(_chatState.value.filter { it.id != deletedChat.id})
                    }
                    else -> {
                        if (wsMessage.type == "ERROR") {
                            println("Error from server: ${wsMessage.message}, status: ${wsMessage.statusCode}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatRepository", "Error parsing message: ${e.message}")
            }
        }
    }

    // Gửi tin nhắn tạo chat
    fun createChat(content: String?, commentPicId: String?, communityId: String, senderId: String) {
        val payload = mapOf(
            "content" to content,
            "communityId" to communityId,
            "senderId" to senderId,
            "commentPicId" to commentPicId
        )
        sendMessage("CREATE_CHAT", payload)
    }

    // Gửi tin nhắn cập nhật chat
    fun updateChat(chatId: String, content: String?, commentPicId: String?) {
        val payload = mapOf(
            "chatId" to chatId,
            "content" to content,
            "commentPicId" to commentPicId
        )
        sendMessage("UPDATE_CHAT", payload)
    }

    // Gửi tin nhắn xóa chat
    fun deleteChat(chatId: String) {
        val payload = mapOf("chatId" to chatId)
        sendMessage("DELETE_CHAT", payload)
    }

    // Gửi yêu cầu lấy danh sách chat
    fun fetchChats(communityId: String) {
        val payload = mapOf("communityId" to communityId)
        sendMessage("FETCH_CHAT_BY_COMMUNITY", payload)
    }

    // Gửi tin nhắn tới server
    private fun sendMessage(action: String, payload: Any) {
        val message = WebSocketMessage(action = action, payload = payload)
        val json = gson.toJson(message)
        webSocketManager.sendMessage(room = "Chat", message = json)
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {
        webSocketManager.disconnect(room = "Chat")
    }
}