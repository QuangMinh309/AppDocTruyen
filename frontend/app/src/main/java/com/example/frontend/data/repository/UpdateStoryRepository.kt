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
class UpdateStoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) {

    suspend fun getStoryById(storyId: Int): Result<Story> {
        Log.d("UpdateStoryRepository", "Fetching story with ID: $storyId")
        return try {
            val response = apiService.getStoryById(storyId)
            Log.d("UpdateStoryRepository", "GetStoryById Request URL: https://062d-116-110-41-191.ngrok-free.app/api/stories/$storyId")
            Log.d("UpdateStoryRepository", "GetStoryById Response - Code: ${response.code()}, Body: ${response.body()?.data}")
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Log.d("UpdateStoryRepository", "Story data: name=${story.name}, coverImgId=${story.coverImgId}, " +
                            "categories=${story.categories?.map { it.id }}, chapters=${story.chapters?.size}")
                    Result.Success(story)
                } ?: run {
                    Log.e("UpdateStoryRepository", "No story data in response")
                    Result.Failure(Exception("No story data in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UpdateStoryRepository", "Failed to fetch story: Code=${response.code()}, Error=$errorBody")
                val errorResponse = errorBody?.let { gson.fromJson(it, com.example.frontend.data.api.ApiError::class.java) }
                Result.Failure(Exception("Failed to fetch story: ${errorResponse?.message ?: response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateStoryRepository", "Exception during getStoryById: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun updateStory(
        storyId: Int,
        storyName: String?,
        description: String?,
        categories: List<Int>,
        pricePerChapter: Float?,
        coverImage: File?
    ): Result<Story> {
        Log.d("UpdateStoryRepository", "Updating story: $storyId, name: $storyName, categories: $categories")
        return try {
            // Chuyển đổi categories thành JSON string
            val categoriesJson = GsonBuilder().create().toJson(categories)
            val categoriesRequestBody = categoriesJson.toRequestBody("application/json".toMediaTypeOrNull())

            // Tạo RequestBody cho các trường khác
            val storyNameRequestBody = storyName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionRequestBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val pricePerChapterRequestBody = pricePerChapter?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Tạo MultipartBody.Part cho coverImage
            val coverImagePart = coverImage?.let {
                val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("coverImgId", it.name, requestBody)
            }

            Log.d("UpdateStoryRepository", "Sending update request: storyName=$storyName, description=$description, " +
                    "categories=$categoriesJson, pricePerChapter=$pricePerChapter, coverImage=${coverImage?.name}")

            val response = apiService.updateStory(
                storyId = storyId,
                storyName = storyNameRequestBody,
                description = descriptionRequestBody,
                categories = categoriesRequestBody,
                pricePerChapter = pricePerChapterRequestBody,
                coverImage = coverImagePart,
                status = null
            )

            Log.d("UpdateStoryRepository", "UpdateStory Request URL: https://062d-116-110-41-191.ngrok-free.app/api/stories/$storyId")
            Log.d("UpdateStoryRepository", "UpdateStory Response - Code: ${response.code()}, Body: ${response.body()?.data}")
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Log.d("UpdateStoryRepository", "Updated story: name=${story.name}, coverImgId=${story.coverImgId}")
                    Result.Success(story)
                } ?: run {
                    Log.e("UpdateStoryRepository", "No story data in response")
                    Result.Failure(Exception("No story data in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UpdateStoryRepository", "Failed to update story: Code=${response.code()}, Error=$errorBody")
                val errorResponse = errorBody?.let { gson.fromJson(it, com.example.frontend.data.api.ApiError::class.java) }
                Result.Failure(Exception("Failed to update story: ${errorResponse?.message ?: response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateStoryRepository", "Exception during updateStory: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        Log.d("UpdateStoryRepository", "Fetching categories")
        return try {
            val response = apiService.getCategories()
            Log.d("UpdateStoryRepository", "GetCategories Request URL: https://062d-116-110-41-191.ngrok-free.app/api/categories")
            Log.d("UpdateStoryRepository", "GetCategories Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    Log.d("UpdateStoryRepository", "Loaded categories: ${categories.map { it.name }}")
                    Result.Success(categories)
                } ?: run {
                    Log.e("UpdateStoryRepository", "No categories data in response")
                    Result.Failure(Exception("No categories data in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UpdateStoryRepository", "Failed to fetch categories: Code=${response.code()}, Error=$errorBody")
                val errorResponse = errorBody?.let { gson.fromJson(it, com.example.frontend.data.api.ApiError::class.java) }
                Result.Failure(Exception("Failed to fetch categories: ${errorResponse?.message ?: response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateStoryRepository", "Exception during getCategories: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun deleteChapter(chapterId: Int): Result<ApiService.DeleteChapterResponse> {
        Log.d("UpdateStoryRepository", "Deleting chapter $chapterId")
        return try {
            val response = apiService.deleteChapter(chapterId)
            Log.d("UpdateStoryRepository", "DeleteChapter Request URL: https://062d-116-110-41-191.ngrok-free.app/api/chapters/$chapterId")
            Log.d("UpdateStoryRepository", "DeleteChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    Log.d("UpdateStoryRepository", "Chapter $chapterId deleted: ${result.message}")
                    Result.Success(result)
                } ?: run {
                    Log.e("UpdateStoryRepository", "No delete response data")
                    Result.Failure(Exception("No delete response data"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UpdateStoryRepository", "Failed to delete chapter: Code=${response.code()}, Error=$errorBody")
                val errorResponse = errorBody?.let { gson.fromJson(it, com.example.frontend.data.api.ApiError::class.java) }
                Result.Failure(Exception("Failed to delete chapter: ${errorResponse?.message ?: response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateStoryRepository", "Exception during deleteChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }
}