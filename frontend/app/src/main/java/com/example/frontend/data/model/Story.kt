package com.example.frontend.data.model

import java.math.BigDecimal
import java.time.LocalDate

data class Story (
    val id: Int,
    val name: String,
    val author: Author,
    val description: String?,
    val ageRange: Int,
    val categories: List<Category>,
    val viewNum: Int ,
    val voteNum: Int,
    val chapterNum :Int,
    val createdAt: LocalDate,
    val updateAt: LocalDate,
    val status: String,
    val price: BigDecimal,
    val pricePerChapter: BigDecimal,
    val coverImgUrl: String
)