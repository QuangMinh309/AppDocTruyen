package com.example.frontend.domain

data class  ReadListItemModel (
    val id: String,
    val name: String,
    val date: String,
    val description: String,
    val stories: List<StoryItemModel>
)