package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import java.io.File
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
            Log.e("apiError","Error: ${error.message}")
            url = ""
        }
        return url
    }
    suspend fun uploadImage(file: File?): Pair<String,String>? {
        if (file == null) return null

        val urlResult = repository.uploadImageToServer(file)
        var imageInfo:Pair<String,String>?=null
        urlResult.onSuccess { info ->
            imageInfo=info
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
        }
        return imageInfo
    }
}
