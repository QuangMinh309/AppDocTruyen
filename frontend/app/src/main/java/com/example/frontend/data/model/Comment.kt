package com.example.frontend.data.model

import java.time.LocalDateTime

data class Comment (
    val commentId: Int,
    val user: User?,
    val chapter: Chapter,
    val content: String?,
    val commentPicId: String?,
    val createAt: LocalDateTime,
    val likeNumber: Int,
    val disLikeNumber:Int
)