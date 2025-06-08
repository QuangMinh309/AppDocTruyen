package com.example.frontend.data.model

import java.time.LocalDateTime

data class Chat (
    val id: Int,
    val communityId: Int,
    val sender: User,
    val content: String?,
    val messagePicUrl: String?,
    val time: LocalDateTime
)
