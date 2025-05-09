package com.example.frontend.data.util

import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.ImageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUrlProvider @Inject constructor(
    private val repository: ImageRepository
) {
    suspend fun fetchImage(imageId: String): Result<String> {
        return repository.getImageUrl(imageId)
    }
}
