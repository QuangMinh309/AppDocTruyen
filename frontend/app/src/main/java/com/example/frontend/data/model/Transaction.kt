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