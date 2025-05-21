package com.example.frontend.data.model

import java.math.BigDecimal
import java.time.LocalDate

data class User (
    val id: Int,
    val name: String,
    val role: Role,
    val dName: String,
    val mail: String,
    val password: String? = null,
    val about: String? = null,
    val dob: LocalDate,
    var followerNum: Int,
    val novelsNum: Int = 0,
    val readListNum: Int = 0,
    val avatarUrl: String? = null,
    val backgroundUrl: String? = null,
    val wallet: BigDecimal = BigDecimal(0),
    val isPremium: Boolean = false
){
    fun plusFollowerNum(){
        ++followerNum
    }
}