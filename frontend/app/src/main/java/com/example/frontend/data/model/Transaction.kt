package com.example.frontend.data.model

import java.time.LocalDateTime

data class Transaction (
    val transactionId: Int,
    val user: User?=null,
    val money: Int=0,
    val type: String,
    val time: LocalDateTime,
    val status: String,
    val finishAt: LocalDateTime?=null
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