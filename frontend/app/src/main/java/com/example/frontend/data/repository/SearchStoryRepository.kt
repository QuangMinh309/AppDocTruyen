package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchStoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun searchStories(searchTerm: String, limit: Int = 20, lastId: Int? = null): Result<List<Story>> {
        return try {
            val response = apiService.searchStories(searchTerm,  lastId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.Success(response.body()?.data?.stories ?: emptyList())
            } else {
                Result.Failure(Exception("Failed to search stories: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getStoriesByCategoryAndStatus(
        categoryId: Int,
        status: String,
//        limit: Int = 20,
//        lastId: Int? = null
    ): Result<List<Story>> {
        return try {
            val response = apiService.getStoriesByCategoryAndStatus(categoryId, status
            //    , limit, lastId
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.Success(response.body()?.data?.stories ?: emptyList())
            } else {
                Result.Failure(Exception("Failed to get stories by category and status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Failed to get categories: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}