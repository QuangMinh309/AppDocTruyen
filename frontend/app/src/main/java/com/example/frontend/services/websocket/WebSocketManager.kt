package com.example.frontend.services.websocket

import android.util.Log
import com.example.frontend.util.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
    class WebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @Named("WebSocketUrl") private val wsBaseUrl: String,
    private val tokenManager: TokenManager)
{

    private val webSockets = mutableMapOf<String, WebSocket>() // Lưu trữ các WebSocket theo room
    private val listeners = mutableMapOf<String, MutableSet<WebSocketListener>>() // Lưu trữ listener cho từng room
    private val isConnected = mutableMapOf<String, Boolean>() // Trạng thái kết nối cho từng room

    suspend fun connect(room: String,bonusQueryString: String) {
        if (isConnected[room] == true) return

        val token = withContext(Dispatchers.IO) {
            tokenManager.getToken()
        }

        val fullUrl = "$wsBaseUrl/$room?token=${token}&$bonusQueryString"
        val request = Request.Builder().url(fullUrl).build()
        val webSocket = okHttpClient.newWebSocket(request, createInternalListener(room))
        webSockets[room] = webSocket
        isConnected[room] = false // Đặt trạng thái ban đầu là false, sẽ cập nhật trong onOpen
        listeners[room] = listeners[room] ?: mutableSetOf()
    }

    fun disconnect(room: String) {
        webSockets[room]?.close(1000, "Client closed")
        webSockets.remove(room)
        isConnected.remove(room)
        listeners.remove(room)
    }

    fun sendMessage(room: String, message: String): Boolean {
        val webSocket = webSockets[room]
        return if (webSocket != null && isConnected[room] == true) {
            val success = webSocket.send(message)
            Log.d("WebSocketManager", "Sent message to $room: $message, Success: $success")
            success
        } else {
            Log.e("WebSocketManager", "Failed to send message to $room: WebSocket not connected")
            false
        }
    }

    fun addListener(room: String, listener: WebSocketListener) {
        listeners[room]?.add(listener) ?: run {
            listeners[room] = mutableSetOf(listener)
        }
    }

    fun removeListener(room: String, listener: WebSocketListener) {
        listeners[room]?.remove(listener)
    }

    private fun createInternalListener(room: String) = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            isConnected[room] = true
            listeners[room]?.forEach { it.onOpen(webSocket, response) }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listeners[room]?.forEach { it.onMessage(webSocket, text) }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            isConnected[room] = false
            listeners[room]?.forEach { it.onClosed(webSocket, code, reason) }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isConnected[room] = false
            listeners[room]?.forEach { it.onFailure(webSocket, t, response) }
            // Thử reconnect nếu cần
            if (isConnected[room] == false){
                CoroutineScope(Dispatchers.IO).launch {
                    connect(room, webSockets[room]?.request()?.url?.toString()?.substringAfter("&") ?: "")
                }
            }
        }
    }
}