package com.example.frontend.data.model

import java.util.Date

data class Comment (
    val commentId: Int,
    val user: User,
    val chapter: Chapter,
    val content: String,
    val commentPicId: String,
    val createAt: Date,
    val likeNumber: Int,
    val disLikeNumber: Int
)