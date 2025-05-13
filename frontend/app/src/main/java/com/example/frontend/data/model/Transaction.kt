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