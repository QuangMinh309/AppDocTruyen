package com.example.frontend.data.repository

import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.ReportRequest
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUserById(userId: Int): Result<User> {
        Log.d("UserProfileRepository", "Fetching user with userId: $userId")
        return try {
            val response = apiService.getUserById(userId)
            Log.d("UserProfileRepository", "GetUserById Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.data?.let { Result.Success(it) } ?: Result.Failure(Exception("No user data in response"))
            } else {
                Result.Failure(Exception("Failed to fetch user: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "Exception during getUserById: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun checkFollowUser(userId: Int): Result<Boolean> {
        Log.d("UserProfileRepository", "Checking follow status for userId: $userId")
        return try {
            val response = apiService.checkFollowUser(userId)
            Log.d("UserProfileRepository", "CheckFollowUser Response - Code: ${response.code()}, Body: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it.isFollowing) } ?: Result.Failure(Exception("No follow status in response"))
            } else {
                Result.Failure(Exception("Failed to check follow: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "Exception during checkFollowUser: ${e.message}", e)
            Result.Failure(e)
        }
    }

    suspend fun getStoriesByUser(userId: Int): Result<List<Story>> {
        Log.d("UserProfileRepository", "Fetching stories for userId: $userId")
        return try {
            val response = apiService.getStoriesByUser(userId)
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                if (storiesResponse?.success == true) {
                    Result.Success(storiesResponse.data.stories)
                } else {
                    Result.Failure(Exception("API returned success: false"))
                }
            } else {
                Result.Failure(Exception("Failed to fetch all stories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun reportUser(userId: Int, reason: String): Result<String> {
        Log.d("UserProfileRepository", "Reporting user with userId: $userId")
        return try{
            val request = ReportRequest(
                reportedUserId = userId,
                reason = reason
            )
            val response = apiService.reportUser(request)
            if (response.isSuccessful) {
                val reportResponse = response.body()
                if (reportResponse?.success == true) {
                    Result.Success(reportResponse.message)
                } else {
                    Result.Failure(Exception("API returned success: false"))
                }
            } else {
                Result.Failure(Exception("Failed to report user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
