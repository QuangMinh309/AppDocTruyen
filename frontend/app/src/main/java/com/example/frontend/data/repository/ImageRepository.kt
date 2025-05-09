package com.example.frontend.data.repository

import com.example.frontend.data.model.Result
import com.example.frontend.data.util.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//@ViewModelScoped
class ImageRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun uploadImageToServer(file: File): Result<String> {
        return try {
            val mediaType = "image/*".toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

            val response = apiService.uploadImage(imagePart)
            if (response.isSuccessful) {
                Result.Success(response.body()?.url ?: "")
            } else {
                Result.Failure(Exception("Upload failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getImageUrl(imageId: String): Result<String> {
        return try {
            val response = apiService.getImageUrl(imageId)
            var url = response.body()?.url
            if (response.isSuccessful) {
                Result.Success(response.body()?.url ?: "")
            } else {
                Result.Failure(Exception("Get URL failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}