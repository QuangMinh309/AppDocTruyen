package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

data class Story(
    @SerializedName("storyId") val id: Int,
    @SerializedName("storyName") val name: String ?,
    @SerializedName("author") val author: Author,
    @SerializedName("description") val description: String?,
    @SerializedName("ageRange") val ageRange: Int?,
    @SerializedName("categories") val categories: List<Category> ?= emptyList(),
    @SerializedName("viewNum") val viewNum: Int,
    @SerializedName("voteNum") val voteNum: Int,
    @SerializedName("chapterNum") val chapterNum: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updateAt: String,
    @SerializedName("status") val status: String,
    @SerializedName("pricePerChapter") val pricePerChapter: BigDecimal,
    @SerializedName("coverImgId") val coverImgId: String,
    @SerializedName("chapters") val chapters: List<Chapter> ?
) {
    val coverImgUrl: String
        get() = "https://res.cloudinary.com/dpqv7ag5w/image/upload/$coverImgId"
}

