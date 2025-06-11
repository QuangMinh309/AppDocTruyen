package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class Community(
//    @SerializedName("communityId") val id: Int,
//    @SerializedName("communitytName") val name: String, // Lưu ý typo "communitytName"
//    @SerializedName("categoryId") val categoryId: Int,
//    val avatarUrl: String? = null,
//    @SerializedName("menberNum") val memberNum: Int,
//    val description: String? = null
    val id: Int,
    val name: String,
    val category:Category,
    val avatarUrl: String,
    val memberNum:Int,
    val description:String
)