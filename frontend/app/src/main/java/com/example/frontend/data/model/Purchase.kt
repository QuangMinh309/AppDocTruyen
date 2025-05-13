package com.example.frontend.data.model

import java.util.Date

data class Purchase (
    val purchasedId: Int,
    val userId: Int,
    val storyId: Int,
    val chapterId: Int,
    val purchasedAt: Date
)