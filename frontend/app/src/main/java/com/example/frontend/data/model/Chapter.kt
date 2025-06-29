package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Chapter(
    @SerializedName("chapterId") val chapterId: Int,
    @SerializedName("chapterName") val chapterName: String,
    @SerializedName("ordinalNumber") val ordinalNumber: Int,
    @SerializedName("storyId") val storyId: Int,
    @SerializedName("content") val content: String?,
    @SerializedName("viewNum") val viewNum: Int,
    @SerializedName("commentNumber") val commentNumber: Int,
    @SerializedName("updatedAt") private val updatedAtString: String?, // Nhận chuỗi từ JSON
    @SerializedName("lockedStatus") val lockedStatus: Boolean
) {
    // Chuyển đổi chuỗi thành LocalDate an toàn
    val updateAt: LocalDate? = updatedAtString?.let {
        try {
            LocalDate.parse(it.split("T")[0], DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            null // Xử lý lỗi chuyển đổi
        }
    }
}