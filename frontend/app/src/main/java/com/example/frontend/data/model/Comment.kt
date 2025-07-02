package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Comment (
    @SerializedName("chatId") val id: Int,
    val chapterName: String,
    val user: User,
    val content: String?="",
    @SerializedName("commentPicUrl") val commentPicUrl: String?,
    val createAt: LocalDateTime,
    val likeNumber: Int=0,
    val isUserLike:Boolean = false
)