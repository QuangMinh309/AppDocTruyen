package com.example.frontend.domain
import com.example.frontend.data.ImageRepository

class ImageUrlProvider {
    private val repository = ImageRepository()
    // Lấy hình từ backend dựa trên imageId
    suspend fun fetchImage(imageId: String): String? {
        return try {
            repository.getImageUrl(imageId)
        } catch (e: Exception) {
            null // Nếu có lỗi, trả về null
        }
    }

}