package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameListRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getNameListById(nameListsId: Int): Result<ApiService.NameListResponse> {
        return try {
            val response = apiService.getNameListById(nameListsId)
            Log.d("NameListRepository", "getNameListById - Request URL: https://062d-116-110-41-191.ngrok-free.app/api/nameLists/$nameListsId")
            Log.d("NameListRepository", "getNameListById - Response Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Log.e("NameListRepository", "getNameListById - Response body is null")
                    Result.Failure(Exception("Response body is null"))
                }
            } else {
                Log.e("NameListRepository", "getNameListById - Failed: ${response.message()}")
                Result.Failure(Exception("Failed to fetch name list: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("NameListRepository", "getNameListById - Exception: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun deleteStoryInNameList(nameListId: Int, storyId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteStoryInNameList(nameListId, storyId)
            Log.d("NameListRepository", "deleteStoryInNameList - Request URL: https://062d-116-110-41-191.ngrok-free.app/api/nameLists/$nameListId/stories/$storyId")
            Log.d("NameListRepository", "deleteStoryInNameList - Response Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(Unit)
                } else {
                    Log.e("NameListRepository", "deleteStoryInNameList - Response body is null")
                    Result.Failure(Exception("Response body is null"))
                }
            } else {
                Log.e("NameListRepository", "deleteStoryInNameList - Failed: ${response.message()}")
                Result.Failure(Exception("Failed to delete story from name list: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("NameListRepository", "deleteStoryInNameList - Exception: ${e.message}", e)
            Result.Failure(e)
        }
    }
}