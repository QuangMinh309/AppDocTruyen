package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class Category (
    @SerializedName("categoryId")val id: Int,
    @SerializedName("categoryName")val name: String?
)