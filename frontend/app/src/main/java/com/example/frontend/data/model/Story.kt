package com.example.frontend.data.model

import java.time.LocalDate

data class Story (
    val storyId: Int,
    val storyName: String,
    val userId: Int,
    val title: String,
    val description: String,
    val ageRange: Int,
    val viewNum: Int,
    val voteNum: Int,
    val createdAt: LocalDate,
    val updateAt: LocalDate,
    val status: String,
    val price: Float,
    val pricePerChapter: Float,
    val coverImgId: String
)