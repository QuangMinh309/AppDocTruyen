package com.example.frontend.data.model

import java.time.LocalDate

data class Notification (
    val notificationId: Int,
    val type: String,
    val content: String,
    val refId: Int,
    val status: String,
    val createAt: LocalDate
)