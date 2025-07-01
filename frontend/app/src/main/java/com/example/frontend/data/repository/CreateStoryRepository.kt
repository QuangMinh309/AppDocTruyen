package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateStoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) {
    data class CreateStoryRequest(
        val storyName: String,
        val description: String?,
        val categories: List<Int>,
        val pricePerChapter: Float? = null // Tùy chọn để khớp với backend hiện tại
    )

    suspend fun getCategories(): Result<List<Category>> {
        Log.d("CreateStoryRepository", "Fetching categories")
        return try {
            val response = apiService.getCategories()
            Log.d("CreateStoryRepository", "GetCategories Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) }
                    ?: Result.Failure(Exception("No categories data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, com.example.frontend.data.api.ApiError::class.java)
                Result.Failure(Exception("Failed to fetch categories: ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Log.e("CreateStoryRepository", "Exception during getCategories: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun createStory(
        storyName: String,
        description: String?,
        categories: List<Int>,
        pricePerChapter: Float?,
        coverImage: File?
    ): Result<Story> {
        Log.d("CreateStoryRepository", "Creating story: $storyName, categories: $categories")
        return try {
            var coverImgId: String? = null
            if (coverImage != null) {
                val mediaType = "image/*".toMediaTypeOrNull()
                val requestBody = coverImage.asRequestBody(mediaType)
                val imagePart = MultipartBody.Part.createFormData("image", coverImage.name, requestBody)
                val uploadResponse = apiService.uploadImage(imagePart)
                if (uploadResponse.isSuccessful) {
                    coverImgId = uploadResponse.body()?.publicId
                } else {
                    Log.e("CreateStoryRepository", "Image upload failed: ${uploadResponse.code()}")
                    return Result.Failure(Exception("Image upload failed: ${uploadResponse.message()}"))
                }
            }

            val request = CreateStoryRequest(
                storyName = storyName,
                description = description,
                categories = categories,
                pricePerChapter = pricePerChapter
            )
            val response = apiService.createStory(request)
            Log.d("CreateStoryRepository", "CreateStory Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) }
                    ?: Result.Failure(Exception("No story data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, com.example.frontend.data.api.ApiError::class.java)
                Result.Failure(Exception("Failed to create story: ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Log.e("CreateStoryRepository", "Exception during createStory: ${e.message}", e)
            Result.Failure(e)
        }
    }
}