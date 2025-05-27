package com.example.frontend.data.model

data class NameList (
    val id: Int,
    val name: String,
    val stories: List<Story>,
    val userId: Int,
    val description: String
)