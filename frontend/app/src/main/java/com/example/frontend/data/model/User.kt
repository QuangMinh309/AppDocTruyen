package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

data class User(
    @SerializedName("userId") val id: Int,
    @SerializedName("dUserName") val dName: String,
    val name: String = "",
    val role: Role? = null,
    val mail: String? = null,
    val password: String? = null,
    val about: String? = null,
    val dob: LocalDate? = null,
    var followerNum: Int? = null,
    val novelsNum: Int? = null,
    val readListNum: Int? = null,
    val avatarUrl: String? = null,
    val backgroundUrl: String? = null,
    val wallet: BigDecimal? = null,
    val isPremium: Boolean? = null
){
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