package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WriteRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun createChapter(storyId: Int, request: ApiService.CreateChapterRequest): Result<Chapter> {
        Log.d("WriteRepository", "Creating chapter for storyId: $storyId with name: ${request.chapterName}")
        return try {
            val response = apiService.createChapter(storyId, request)
            Log.d("WriteRepository", "CreateChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No chapter data in response"))
            } else {
                Result.Failure(Exception("Failed to create chapter: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("WriteRepository", "Exception during createChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }
}