package com.example.frontend.domain

data class StoryItemModel (
    val id: String,
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