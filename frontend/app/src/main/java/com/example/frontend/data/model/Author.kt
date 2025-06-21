package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("userId") val id: Int,
    @SerializedName("userName") val name: String,
    @SerializedName("dUserName") var dName:String ?,
    @SerializedName("avatarId") val avatarId: String?
)
{
    val avatarUrl: String
        get()="https://res.cloudinary.com/dpqv7ag5w/image/upload/$avatarId"
}