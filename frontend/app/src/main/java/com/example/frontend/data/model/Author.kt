package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("userId") val id: Int,
    @SerializedName("userName") val name: String,
    @SerializedName("avatarId") val avatarId: String?
)