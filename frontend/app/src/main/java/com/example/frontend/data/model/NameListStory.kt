package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class NameListStory(
    @SerializedName("coverImgId") val coverImgId: String
){
    val coverImgUrl: String
        get() = "https://res.cloudinary.com/dpqv7ag5w/image/upload/$coverImgId"
}
