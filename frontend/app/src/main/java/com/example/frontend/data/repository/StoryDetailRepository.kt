package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryDetailRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getStoryById(storyId: Int): Result<Story> {
        return try {
            val response = apiService.getStoryById(storyId)
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Result.Success(story)
                } ?: Result.Failure(Exception("No story data in response"))
            } else {
                Result.Failure(Exception("Failed to fetch story: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getAllStories(): Result<List<Story>> {
        return try {
            val response = apiService.getAllStories()
            if (response.isSuccessful) {
                response.body()?.data?.stories?.let { stories ->
                    Result.Success(stories)
                } ?: Result.Failure(Exception("No stories data in response"))
            } else {
                Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}