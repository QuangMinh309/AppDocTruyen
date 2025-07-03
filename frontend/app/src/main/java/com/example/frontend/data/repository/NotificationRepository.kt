package com.example.frontend.data.repository

import android.content.Context
import android.util.Log
import com.example.frontend.data.api.CommunityApiService
import com.example.frontend.data.api.NotificationApiService
import com.example.frontend.data.model.Notification
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.Result
import com.example.frontend.services.NotificationHelper
import com.example.frontend.services.websocket.WebSocketManager
import com.google.gson.Gson
import com.google.gson.JsonSerializationContext
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


class NotificationRepository @Inject constructor(
    private val apiService: NotificationApiService,
    private val webSocketManager: WebSocketManager,
    private val appContext: Context,
    private val gson: Gson,
) {

    private val roomName = "notification"
    private val _isConnected = MutableStateFlow(false)
    val isConnected  : StateFlow<Boolean> = _isConnected



    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("NotificationRepository", "WebSocket opened!")
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            handleWebSocketMessage(text)
        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Error: ${t.message}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.w("NotificationRepository", "WebSocket closing: $reason, code: $code")
            webSocket.close(1000, null)
        }
    }

    init {
        // Gán listener cho WebSocketManager
        webSocketManager. addListener(room = roomName,listener=listener)
    }

    suspend fun connect() {

        webSocketManager.connect(  room = roomName, bonusQueryString = "" )
    }

    private suspend fun reconnect() {
        // Logic reconnect (tùy thuộc vào WebSocketManager)
        delay(2000) // Delay trước khi reconnect
        connect()
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
                    "BRC_NOTIFICATION"-> {
                        scope.launch(Dispatchers.Main) {
                            NotificationHelper.showNotification(
                                context = appContext, // truyền vào từ constructor hoặc Singleton
                                title = "Bạn có tin nhắn mới!",
                                content = wsMessage.message ?: "Xem ngay tin nhắn mới"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NotificationRepository", "Error parsing message: ${e.message}")
            }
        }
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {
        webSocketManager.disconnect(room = roomName)
        webSocketManager.removeListener(room = roomName, listener = listener)

    }

    suspend fun getNotificationById(): Result<List<Notification>> {
        return try {
            val response = apiService.getNotificationById()
            if (response.isSuccessful) {
                Result.Success(response.body()?.data ?: throw Exception("Notification not found"))

            } else {
                Result.Failure(Exception("Get community failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteNotification(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteNotification(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Delete community failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
        suspend fun getUnreadNotificationCount(): Result<Int> {
        return try {
            val response = apiService.getUnreadNotificationById()
            if (response.isSuccessful) {
                Result.Success(response.body()?.data ?: 0)
            } else {
                Result.Failure(Exception("Delete community failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


}