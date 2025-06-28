package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.ListTransactionResponse
import com.example.frontend.data.api.NoDataResponse
import com.example.frontend.data.api.TransactionRequest
import com.example.frontend.data.api.UserFollowRequest
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Transaction
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
    suspend fun purchasePremium(): Result<NoDataResponse> {
        return try {
            val response = apiService.purchasePremium()
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("Unfollow failed"))

            } else {
                Result.Failure(Exception("Purchase premium failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun createTransaction(trans:Transaction): Result<Transaction> {
        return try {
            val response = apiService.createTransaction(
                TransactionRequest(trans.user?.id?:0,trans.money,trans.type)
            )
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("transaction failed"))

            } else {
                Result.Failure(Exception("Create transaction failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getUserTransaction(lastId:Int?=0,id:Int?): Result<ListTransactionResponse> {
        return try {
            if(id==null) return Result.Failure(Exception("id is null"))

            val response = apiService.getAllUserTransaction(id,lastId?:20)
            if (response.isSuccessful) {
                Result.Success( response.body() ?:  throw Exception("transaction failed"))
            } else {
                Result.Failure(Exception("Fetch user's transactions failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}