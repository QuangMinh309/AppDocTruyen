package com.example.frontend.data.model

import java.time.LocalDate

data class Chapter (
    val chapterId: Int,
    val chapterName: String,
    val ordinalNumber: Int,
    val storyId: Int,
    val content: String,
    val viewNum: Int,
    val commentNumber: Int,
    val updateAt: LocalDate,
    val lockedStatus: Boolean
)