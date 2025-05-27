package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStories(): Result<List<Story>> {
        return try {
            val response = apiService.getStories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getStoryById(id: Int): Result<Story> {
        return try {
            val response = apiService.getStoryById(id)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Story not found"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun searchStories(query: String): Result<List<Story>> {
        return try {
            val response = apiService.searchStories(query)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Search failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun createStory(story: Story): Result<Story> {
        return try {
            val response = apiService.createStory(story)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Creation failed"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateStory(id: Int, story: Story): Result<Story> {
        return try {
            val response = apiService.updateStory(id, story)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Update failed"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteStory(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteStory(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}