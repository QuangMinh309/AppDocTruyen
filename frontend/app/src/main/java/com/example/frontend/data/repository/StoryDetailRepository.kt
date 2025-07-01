package com.example.frontend.data.repository

import android.util.Log
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

    suspend fun updateStory(storyId: Int, status: String): Result<Story> {
        Log.d("StoryDetailRepository", "Updating story $storyId with status: $status")
        return try {
            val response = apiService.updateStory(storyId, ApiService.StoryUpdateRequest(status))
            Log.d("StoryDetailRepository", "API Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Result.Success(story)
                } ?: Result.Failure(Exception("No story data in response"))
            } else {
                Result.Failure(Exception("Failed to update story: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during API call: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun checkVote(storyId: Int): Result<ApiService.CheckVoteResponse> {
        Log.d("StoryDetailRepository", "Checking vote status for storyId: $storyId")
        return try {
            val response = apiService.checkVote(storyId)
            Log.d("StoryDetailRepository", "CheckVote Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) } ?: Result.Failure(Exception("No vote data in response"))
            } else {
                Result.Failure(Exception("Failed to check vote: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during checkVote: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun voteStory(storyId: Int): Result<ApiService.VoteStoryResponse> {
        Log.d("StoryDetailRepository", "Voting for storyId: $storyId")
        return try {
            val response = apiService.voteStory(storyId)
            Log.d("StoryDetailRepository", "VoteStory Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) } ?: Result.Failure(Exception("No vote response data"))
            } else {
                Result.Failure(Exception("Failed to vote: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during voteStory: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun deleteChapter(chapterId: Int): Result<ApiService.DeleteChapterResponse> {
        Log.d("StoryDetailRepository", "Deleting chapter $chapterId")
        return try {
            val response = apiService.deleteChapter(chapterId)
            Log.d("StoryDetailRepository", "DeleteChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) } ?: Result.Failure(Exception("No delete response data"))
            } else {
                Result.Failure(Exception("Failed to delete chapter: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during deleteChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun deleteStory(storyId: Int): Result<ApiService.StoryDeleteResponse> {
        Log.d("StoryDetailRepository", "Deleting story $storyId")
        return try {
            val response = apiService.deleteStory(storyId)
            Log.d("StoryDetailRepository", "DeleteStory Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) } ?: Result.Failure(Exception("No delete response data"))
                } else {
                Result.Failure(Exception("Failed to delete story: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during deleteStory: ${e.message}", e)
            Result.Failure(e)
        }
    }
}