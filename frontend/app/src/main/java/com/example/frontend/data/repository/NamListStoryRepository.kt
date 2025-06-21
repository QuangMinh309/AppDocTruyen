package com.example.frontend.data.repository

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
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Failure(Exception("Response body is null"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch name list: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}