package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.DeleteCategoryResponse
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun purchaseChapter(chapterId: Int): Result<String> {
        return try {
            if(chapterId==0)
                return  Result.Failure(Exception("Chapter id is not available"))
            val response = apiService.purchaseChapter(chapterId)
            Log.d("UpdateChapterRepository", "UpdateChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.message?.let { Result.Success(it) } ?: Result.Failure(Exception("No message in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("UpdateChapterRepository", "Exception during updateChapter: ${e.message}", e)
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateStory(storyId: Int, status: String): Result<Story> {
        Log.d("StoryDetailRepository", "Updating story $storyId with status: $status")
        return try {
            val statusRequestBody = status.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = apiService.updateStory(
                storyId = storyId,
                storyName = null,
                description = null,
                categories = null,
                pricePerChapter = null,
                status = statusRequestBody,
                coverImage = null
            )
            Log.d("StoryDetailRepository", "API Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { story ->
                    Result.Success(story)
                } ?: Result.Failure(Exception("No story data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
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
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("StoryDetailRepository", "Exception during deleteStory: ${e.message}", e)
            Result.Failure(e)
        }
    }
}

