package com.example.frontend.data.model

import java.util.Date

data class Comment (
    val commentId: Int,
    val userId: Int,
    val chapterId: Int,
    val content: String,
    val commentPicId: String,
    val createAt: Date
)