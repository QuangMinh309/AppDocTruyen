package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YourStoryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStoriesByUser(userId: Int, limit: Int = 20, lastId: Int? = null): Result<List<Story>> {
        return try {
            val response = apiService.getStoriesByUser(userId, lastId)
            Log.d("YourStoryRepository", "getStoriesByUser - Request URL: https://062d-116-110-41-191.ngrok-free.app/api/stories/user/$userId?limit=$limit&lastId=$lastId")
            Log.d("YourStoryRepository", "getStoriesByUser - Response Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Log.e("YourStoryRepository", "getStoriesByUser - API returned success: false")
                    Result.Failure(Exception("API returned success: false"))
                }
            } else {
                Log.e("YourStoryRepository", "getStoriesByUser - Failed: ${response.message()}")
                Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("YourStoryRepository", "getStoriesByUser - Exception: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun deleteStory(storyId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteStory(storyId)
            Log.d("YourStoryRepository", "deleteStory - Request URL: https://062d-116-110-41-191.ngrok-free.app/api/stories/$storyId")
            Log.d("YourStoryRepository", "deleteStory - Response Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                val deleteResponse = response.body()
                if (deleteResponse?.success == true) {
                    Result.Success(Unit)
                } else {
                    Log.e("YourStoryRepository", "deleteStory - API returned success: false, message: ${deleteResponse?.message}")
                    Result.Failure(Exception("API returned success: false: ${deleteResponse?.message}"))
                }
            } else {
                Log.e("YourStoryRepository", "deleteStory - Failed: ${response.message()}")
                Result.Failure(Exception("Failed to delete story: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("YourStoryRepository", "deleteStory - Exception: ${e.message}", e)
            Result.Failure(e)
        }
    }
}