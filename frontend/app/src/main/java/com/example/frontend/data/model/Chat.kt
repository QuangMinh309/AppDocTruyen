package com.example.frontend.data.model

import java.util.Date

data class Chat (
    val chatId: Int,
    val communityId: Int,
    val senderId: Int,
    val content: String,
    val commentPicId: String,
    val time: Date
)