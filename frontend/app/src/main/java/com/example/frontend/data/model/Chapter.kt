package com.example.frontend.data.model

import java.time.LocalDate
import java.util.Date

data class Chapter (
    val chapterId: Int,
    val chapterName: String,
    val OrdinalNumber: Int,
    val storyId: Int,
    val content: String,
    val viewNum: Int,
    val commentNumber: Int,
    val UpdateAt: Date,
    val lockedStatus: Boolean
)