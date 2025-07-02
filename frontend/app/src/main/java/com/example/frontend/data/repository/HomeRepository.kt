package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(

    private val apiService: ApiService
) {
    suspend fun getAllStories(): Result<List<Story>> {
        return try {
            val response = apiService.getAllStories()
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Result.Failure(Exception("API returned success: false"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch all stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getStoriesByUpdateDate(): Result<List<Story>> {
        return try {
            val response = apiService.getStoriesByUpdateDate()
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Result.Failure(Exception("Failed to fetch updated stories: ${response.message()}"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch updated stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

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

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                val categories = response.body()
                if (categories != null) {
                    Result.Success(categories)
                } else {
                    Result.Failure(Exception("API returned null categories"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch categories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getUserReadingLists(): Result<List<NameList>> {
        return try {
            val response = apiService.getUserReadingLists()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body.readingLists)
                } else {
                    Result.Failure(Exception("API returned null data"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch user reading lists: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}