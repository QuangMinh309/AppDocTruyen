package com.example.frontend.data.model

data class StoryItemModel (
    val id: Int,
    val title: String,
    val author: String,
    val coverImage: String,
    val isPremium: Boolean,
    val shortDescription: String,
    val likes: Long,
    val chaptersnumbers: Int,
    val genres: List<String>,
    val lastUpdated: String,
    val views: Long

)