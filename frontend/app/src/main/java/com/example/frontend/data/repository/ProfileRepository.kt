package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getReadLists(): Result<List<NameList>> {
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
                Result.Failure(Exception("Failed to fetch read lists: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateNameList(nameListId: Int, name: String, description: String): Result<Unit> {
        return try {
            val request =
                ApiService.UpdateNameListRequest(nameList = name, description = description)
            val response = apiService.updateNameList(nameListId, request)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Failed to update read list: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteNameList(nameListId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteNameList(nameListId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Failed to delete read list: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}