package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryStoryListRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStoriesByCategory(categoryId: Int): Result<List<Story>> {
        return try {
            val response = apiService.getStoriesByCategory(categoryId)
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.formattedStories)
                } else {
                    Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}