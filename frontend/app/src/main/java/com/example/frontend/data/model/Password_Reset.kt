package com.example.frontend.data.model

import java.time.LocalDate

data class Password_Reset (
    val OTP: String,
    val userId: Int,
    val createdAt: LocalDate,
    val isUsed: Boolean
)