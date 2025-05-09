package com.example.frontend.data.model

import java.util.Date

data class History (
    val historyId: String,
    val userId: Int,
    val chapterId: Int,
    val lastReadAt: Date
)