package com.example.frontend.data.repository

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
            val response = apiService.getStoriesByUser(userId, limit, lastId)
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Result.Failure(Exception("API returned success: false"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}