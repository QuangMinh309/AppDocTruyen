package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUrlProvider @Inject constructor(
    private val repository: ImageRepository
) {
    suspend fun fetchImage(imageId: String): String {
        val urlResult = repository.getImageUrl(imageId)
        var url:String=""
        urlResult.onSuccess { imageUrl ->
            url = imageUrl
        }.onFailure { error ->
            Log.e("Get/imageError","Error: ${error.message}")
            url = ""
        }
        return url
    }
}
