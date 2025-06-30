package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class   ProfileRepository @Inject constructor(
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
}