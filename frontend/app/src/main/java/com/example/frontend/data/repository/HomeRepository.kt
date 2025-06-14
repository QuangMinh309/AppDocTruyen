package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStories(): Result<List<Story>> {
        return try {
            val response = apiService.getStories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Failed to fetch stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getNameLists(): Result<List<NameList>> {
        return try {
            val response = apiService.getNameLists()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Failed to fetch name lists: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}