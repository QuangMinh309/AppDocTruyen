package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopRankingRepository @Inject constructor(
    private val apiService: ApiService
)  {
    suspend fun getStoriesByVote(): Result<List<Story>> {
        return try {
            val response = apiService.getStoriesByVote()
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Result.Failure(Exception("Failed to fetch top voted stories: ${response.message()}"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch top voted stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}