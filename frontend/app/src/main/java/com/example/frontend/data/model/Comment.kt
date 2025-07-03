package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Comment (
    @SerializedName("commentId") val id: Int,
    val chapter: Chapter,
    val user: User,
    val content: String?="",
    @SerializedName("commentPicUrl") val commentPicUrl: String?,
    val createAt: LocalDateTime,
    @SerializedName("likeNum")val likeNumber: Int=0,
    val isUserLike:Boolean = false
)