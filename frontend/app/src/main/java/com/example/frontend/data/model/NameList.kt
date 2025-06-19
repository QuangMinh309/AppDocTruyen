package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class NameList(
    @SerializedName("nameListId") val id: Int,
    @SerializedName("nameList") val name: String,
    @SerializedName("stories") val stories: List<NameListStory>,
    @SerializedName("userId") val userId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("storyCount") val storyCount: Int
)