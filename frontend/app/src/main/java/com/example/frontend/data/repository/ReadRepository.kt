package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadRepository @Inject constructor(
    private val apiService: ApiService,

) {

    suspend fun getChapter(chapterId: Int): Result<Chapter> {
        Log.d("ReadRepository", "Fetching chapter with chapterId: $chapterId")
        return try {
            val response = apiService.getChapter(chapterId)
            Log.d("ReadRepository", "GetChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No chapter data in response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrEmpty()) {
                    try {
                        val errorJson = JSONObject(errorBody)
                        errorJson.getString("message")
                    } catch (e: Exception) {
                        "Failed to fetch chapter: ${response.code()} - ${response.message()}"
                    }
                } else {
                    "Failed to fetch chapter: ${response.code()} - ${response.message()}"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("ReadRepository", "Exception during getChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun getNextChapter(chapterId: Int): Result<Chapter> {
        Log.d("ReadRepository", "Fetching next chapter for chapterId: $chapterId")
        return try {
            val response = apiService.getNextChapter(chapterId)
            Log.d("ReadRepository", "GetNextChapter Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No next chapter data in response"))
            } else {
                Result.Failure(Exception("Failed to fetch next chapter: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ReadRepository", "Exception during getNextChapter: ${e.message}", e)
            Result.Failure(e)
        }
    }
}