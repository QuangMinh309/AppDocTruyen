package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class Community(
    @SerializedName("communityId") val id: Int,
    @SerializedName("communityName") val name: String,
    @SerializedName("category") val category: Category,
    var avatarUrl: String? = null,
    @SerializedName("memberNum") val memberNum: Int,
    val description: String? = null,
    val members: List<User>? = null
){
    public fun copy():Community{
        return Community(
            id = this.id,
            name =  this.name,
            category =  this.category,
            avatarUrl =  this.avatarUrl,
            memberNum =  this.memberNum,
            members =  this.members,
        )
    }
}