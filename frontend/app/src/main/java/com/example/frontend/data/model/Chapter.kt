package com.example.frontend.data.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Chapter(
    @SerializedName("chapterId") val chapterId: Int,
    @SerializedName("chapterName") val chapterName: String,
    @SerializedName("ordinalNumber") val ordinalNumber: Int,
    @SerializedName("storyId") val storyId: Int,
    @SerializedName("content") val content: String?,
    @SerializedName("viewNum") val viewNum: Int=0,
    @SerializedName("commentNumber") val commentNumber: Int=0,
    @SerializedName("updatedAt") val updatedAtString: String?, // Nhận chuỗi từ JSON
    @SerializedName("lockedStatus") val lockedStatus: Boolean
) {
    val updateAt: LocalDate? = updatedAtString?.let { dateStr ->
        try {
            Log.d("Chapter", "Attempting to parse date: '$dateStr'")
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME // Hỗ trợ "yyyy-MM-dd'T'HH:mm:ss.SSSX"
            val dateTime = LocalDateTime.parse(dateStr.trim(), formatter) // Thêm trim() để loại bỏ khoảng trắng thừa
            val localDate = dateTime.toLocalDate()
            Log.d("Chapter", "Successfully parsed updateAt: $localDate")
            localDate
        } catch (e: Exception) {
            Log.e("Chapter", "Failed to parse updateAt: '$dateStr', error: ${e.message}", e)
            null
        }
    } ?: run {
        Log.w("Chapter", "updatedAtString is null or empty: $updatedAtString")
        null
    }
}