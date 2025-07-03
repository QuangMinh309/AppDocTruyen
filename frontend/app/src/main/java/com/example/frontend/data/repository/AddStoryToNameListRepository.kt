package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddStoryToNameListRepository @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) {
    suspend fun getUserReadingLists(): Result<List<NameList>> {
        Log.d("AddStoryToNameListRepository", "Fetching user reading lists")
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
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, com.example.frontend.data.api.ApiError::class.java)
                Result.Failure(Exception("Failed to fetch user reading lists: ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Log.e("AddStoryToNameListRepository", "Exception during getUserReadingLists: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun addStoryToNameList(nameListId: Int, storyId: Int): Result<ApiService.AddStoryToNameListResponse> {
        Log.d("AddStoryToNameListRepository", "Adding story $storyId to nameList $nameListId")
        return try {
            val request = ApiService.AddStoryToNameListRequest(storyId = storyId)
            val response = apiService.addStoryToNameList(nameListId, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) }
                    ?: Result.Failure(Exception("No data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, com.example.frontend.data.api.ApiError::class.java)
                Result.Failure(Exception("${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Log.e("AddStoryToNameListRepository", "Exception during addStoryToNameList: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun createNameList(nameList: String, description: String): Result<ApiService.CreateNameListResponse> {
        Log.d("AddStoryToNameListRepository", "Creating nameList: $nameList")
        return try {
            val request = ApiService.CreateNameListRequest(nameList = nameList, description = description)
            val response = apiService.createNameList(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) }
                    ?: Result.Failure(Exception("No data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, com.example.frontend.data.api.ApiError::class.java)
                Result.Failure(Exception("Failed to create name list: ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Log.e("AddStoryToNameListRepository", "Exception during createNameList: ${e.message}", e)
            Result.Failure(e)
        }
    }
}