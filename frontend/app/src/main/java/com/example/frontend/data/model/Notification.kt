package com.example.frontend.data.model


import java.time.LocalDateTime

data class Notification (
    val notificationId: Int,
    val type: String,
    val content: String,
    val refId: Int,
    val status: String,
    val createAt: LocalDateTime
)