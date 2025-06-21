package com.example.frontend.services.websocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(private val okHttpClient: OkHttpClient,
                                           private val baseUrl: String){

    private var webSocket: WebSocket? = null
    private val listeners = mutableSetOf<WebSocketListener>()
    private var isConnected = false

    fun connect() {
        if (isConnected) return
        val request = Request.Builder().url(baseUrl).build()
        webSocket = okHttpClient.newWebSocket(request, internalListener)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
        isConnected = false
    }

    fun sendMessage(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }

    fun addListener(listener: WebSocketListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: WebSocketListener) {
        listeners.remove(listener)
    }

    private val internalListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            isConnected = true
            listeners.forEach { it.onOpen(webSocket, response) }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listeners.forEach { it.onMessage(webSocket, text) }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            isConnected = false
            listeners.forEach { it.onClosed(webSocket, code, reason) }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isConnected = false
            listeners.forEach { it.onFailure(webSocket, t, response) }
            // Bạn có thể thêm logic reconnect ở đây nếu muốn
        }
    }
}

