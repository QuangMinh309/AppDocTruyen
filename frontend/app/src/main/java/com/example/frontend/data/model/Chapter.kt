package com.example.frontend.data.model

import java.time.LocalDate

data class Chapter (
    val chapterId: Int,
    val chapterName: String,
    val OrdinalNumber: Int,
    val storyId: Int,
    val content: String,
    val viewNum: Int,
    val UpdateAt: LocalDate,
    val lockedStatus: Boolean
)