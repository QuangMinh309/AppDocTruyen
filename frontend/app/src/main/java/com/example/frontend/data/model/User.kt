package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class User(
    @SerializedName("userId") val id: Int,
    @SerializedName("dUserName") val dName: String,
    @SerializedName("userName")val name: String = "",
    val role: Role? = null,
    val mail: String? = null,
    val password: String? = null,
    val about: String? = null,
   @SerializedName("DOB") val dob: String? = null,
    var followerNum: Int? = null,
   @SerializedName("storyCount") val novelsNum: Int? = null,
    @SerializedName("nameListCount") val readListNum: Int? = null,
    val avatarId: String? = null,
    val backgroundId: String? = null,
    val wallet: BigDecimal? = null,
    val isPremium: Boolean? = null,
    @SerializedName("avatarUrl") val avatarUrls: String?=null,
    var isFollowed : Boolean = false,
    var status : String? = null,
){
    val avatarUrl: String
        get() = "https://res.cloudinary.com/dpqv7ag5w/image/upload/$avatarId"

    val backgroundUrl: String
        get() = "https://res.cloudinary.com/dpqv7ag5w/image/upload/$backgroundId"

    fun plusFollowerNum(){
        followerNum.let{it->
            followerNum = it?.plus(1)
        }
    }
    fun minusFollowerNum(){
        followerNum.let{it->
            followerNum = it?.minus(1)
        }
    }
}