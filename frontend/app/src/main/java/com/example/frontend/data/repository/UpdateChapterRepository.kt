package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.DeleteCategoryResponse
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
import com.google.gson.Gson

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateChapterRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getChapter(chapterId: Int): Result<Chapter> {
        Log.d("UpdateChapterRepository", "Fetching chapter with chapterId: $chapterId")
        return try {
            val response = apiService.getChapter(chapterId)
            Log.d("UpdateChapterRepository", "GetChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No chapter data in response"))
            } else {
                Result.Failure(Exception("Failed to fetch chapter: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateChapterRepository", "Exception during getChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun updateChapter(storyId: Int, chapterId: Int, chapterName: String, content: String): Result<Chapter> {
        Log.d("UpdateChapterRepository", "Updating chapter with chapterId: $chapterId, storyId: $storyId")
        return try {
            val request =
                ApiService.UpdateChapterRequest(chapterName = chapterName, content = content)
            val response = apiService.updateChapter(storyId, chapterId, request)
            Log.d("UpdateChapterRepository", "UpdateChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No chapter data in response"))
            } else {
                Result.Failure(Exception("Failed to update chapter: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UpdateChapterRepository", "Exception during updateChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }



}