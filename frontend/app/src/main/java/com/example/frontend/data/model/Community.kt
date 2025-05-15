package com.example.frontend.data.model

data class Community (
    val id: Int,
    val name: String,
    val category: Category,
    val avatarUrl: String,
    val memberNum: Int,
    val description: String
)