package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateStoryRepository @Inject constructor(
    private val apiService: ApiService
) {

    data class CreateStoryRequest(
        val storyName: String,
        val description: String?,
        val categories: List<Int>,
        val pricePerChapter: Float?,
        val status: String?
    )

    suspend fun createStory(
        storyName: String,
        description: String?,
        categories: List<Int>,
        pricePerChapter: Float?,
        coverImage: File?
    ): Result<Story> {
        Log.d("CreateStoryRepository", "Creating story: name=$storyName, categories=$categories, coverImage=${coverImage?.name}")
        return try {
            val categoriesJson = if (categories.isNotEmpty()) {
                GsonBuilder().create().toJson(categories)
            } else {
                "[]"
            }
            val categoriesRequestBody = categoriesJson.toRequestBody("application/json".toMediaTypeOrNull())
            val storyNameRequestBody = storyName.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionRequestBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val pricePerChapterRequestBody = pricePerChapter?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val coverImagePart = coverImage?.let {
                if (!it.exists()) {
                    Log.e("CreateStoryRepository", "Cover image file does not exist: ${it.absolutePath}")
                    null
                } else {
                    Log.d("CreateStoryRepository", "Cover image file exists: ${it.absolutePath}, size: ${it.length()}")
                    val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("coverImgId", it.name, requestBody)
                }
            }

            Log.d("CreateStoryRepository", "Sending create request: storyName=$storyName, description=$description, " +
                    "categories=$categoriesJson, pricePerChapter=$pricePerChapter, coverImage=${coverImage?.name}")

            val response = apiService.createStory(
                storyName = storyNameRequestBody,
                description = descriptionRequestBody,
                categories = categoriesRequestBody,
                pricePerChapter = pricePerChapterRequestBody,
                coverImage = coverImagePart
            )

            Log.d("CreateStoryRepository", "CreateStory Request URL: https://062d-116-110-41-191.ngrok-free.app/api/stories")
            Log.d("CreateStoryRepository", "CreateStory Response - Code: ${response.code()}, Body: ${response.body()?.data}")
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Log.d("CreateStoryRepository", "Created story: name=${story.name}, coverImgId=${story.coverImgId}")
                    Result.Success(story)
                } ?: run {
                    Log.e("CreateStoryRepository", "No story data in response")
                    Result.Failure(Exception("No story data in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("CreateStoryRepository", "Failed to create story: Code=${response.code()}, Error=$errorBody")
              //  val errorResponse = errorBody?.let { GsonBuilder().create().fromJson(it, ApiService.ApiError::class.java) }
                Result.Failure(Exception("Failed to create story: "
                ))
            }
        } catch (e: Exception) {
            Log.e("CreateStoryRepository", "Exception during createStory: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        Log.d("CreateStoryRepository", "Fetching categories")
        return try {
            val response = apiService.getCategories()
            Log.d("CreateStoryRepository", "GetCategories Request URL: https://062d-116-110-41-191.ngrok-free.app/api/categories")
            Log.d("CreateStoryRepository", "GetCategories Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    Log.d("CreateStoryRepository", "Loaded categories: ${categories.map { it.name }}")
                    Result.Success(categories)
                } ?: run {
                    Log.e("CreateStoryRepository", "No categories data in response")
                    Result.Failure(Exception("No categories data in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("CreateStoryRepository", "Failed to fetch categories: Code=${response.code()}, Error=$errorBody")
                Log.e("CreateStoryRepository", "Failed to create story: Code=${response.code()}, Error=$errorBody")
                //  val errorResponse = errorBody?.let { GsonBuilder().create().fromJson(it, ApiService.ApiError::class.java) }
                Result.Failure(Exception("Failed to create story: "
                ))
            }
        } catch (e: Exception) {
            Log.e("CreateStoryRepository", "Exception during getCategories: ${e.message}", e)
            Result.Failure(e)
        }
    }
}