package com.example.frontend.data.model

import androidx.datastore.core.DataStore

data class Community (
    val communityId: Int,
    val communitytName: String,
    val categoryId: Int,
    val avatarId: String,
    val menberNum: Int,
    val description: String
)