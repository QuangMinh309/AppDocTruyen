package com.example.frontend.data.model

data class  ReadListItemModel (
    val id: Int,
    val name: String,
    val date: String,
    val description: String,
    val stories: List<StoryItemModel>
)