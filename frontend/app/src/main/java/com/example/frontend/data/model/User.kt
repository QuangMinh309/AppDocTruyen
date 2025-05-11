package com.example.frontend.data.model

import java.math.BigDecimal
import java.time.LocalDate

data class User (
    val userId: Int,
    val userName: String,
    val roleId: Int,
    val dUserName: String,
    val mail: String,
    val about: String,
    val DOB: LocalDate,
    val followerNum: Int,
    val password: String,
    val avatarId: String,
    val backgroundId: String,
    val wallet: BigDecimal,
    val isPremium: Boolean
)