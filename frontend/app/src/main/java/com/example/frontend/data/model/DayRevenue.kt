package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

data class DayRevenue(
    @SerializedName("date")val date: String,
    @SerializedName("totalIncome")val totalIncome: Int
)