package com.example.frontend.data.model

import java.time.LocalDate

data class Transaction (
    val transactionId: Int,
    val userId: Int,
    val money: Int,
    val type: String,
    val time: LocalDate,
    val status: String,
    val finishAt: LocalDate
)

data class userTransaction (
    val userId : Int,
    val userName : String,
    val dUserName : String,
    val avatarId : String
)

data class Transaction2 (
    val transactionId: Int,
    val userId: Int,
    val money: Int,
    val type: String,
    val time: String,
    val status: String,
    val finishAt: String,
    val user : userTransaction
)