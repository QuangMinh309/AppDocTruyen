package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Chat (
    @SerializedName("chatId") val id: Int,
    val communityId: Int,
    val sender: User,
    val content: String?="",
    @SerializedName("commentPicUrl") val messagePicUrl: String?,
    val time: LocalDateTime,
    val isUser: Boolean=false
)