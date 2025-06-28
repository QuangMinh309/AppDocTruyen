package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.NoDataResponse
import com.example.frontend.data.api.UserFollowRequest
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun follow(user: User): Result<NoDataResponse> {
        return try {
            val request = UserFollowRequest(followedId=user.id)

            val response = apiService.follow(request)
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("follow failed"))

            } else {
                Result.Failure(Exception("Follow user failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun unFollow(user: User): Result<NoDataResponse> {
        return try {
            val request = UserFollowRequest(followedId=user.id)

            val response = apiService.unFollow(request)
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("Unfollow failed"))

            } else {
                Result.Failure(Exception("UnFollow user failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getUserById(id: Int): Result<User> {
        return try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                Result.Success(response.body()?.data ?: throw Exception("User not found"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}