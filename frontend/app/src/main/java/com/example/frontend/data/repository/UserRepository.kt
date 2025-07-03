package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiError
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.IdRequest
import com.example.frontend.data.api.ListTransactionResponse
import com.example.frontend.data.api.NoDataResponse
import com.example.frontend.data.api.TransactionRequest
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.example.frontend.presentation.viewmodel.transaction.BankAccountData
import com.google.gson.Gson
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val gson: Gson,
    private val apiService: ApiService,
) {
    suspend fun follow(user: User): Result<NoDataResponse> {
        return try {
            val request = IdRequest(user.id)

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
            val request = IdRequest(user.id)

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
    suspend fun like(commentId:Int): Result<NoDataResponse> {
        return try {
            val request = IdRequest(commentId)

            val response = apiService.likeComment(request)
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("like failed"))

            } else {
                Result.Failure(Exception("Like comment failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun unlike(commentId: Int): Result<NoDataResponse> {
        return try {
            val request = IdRequest(commentId)

            val response = apiService.unlikeComment(request)
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("Unlike failed"))

            } else {
                Result.Failure(Exception("Unlike user failed with code: ${response.code()}"))
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
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ApiError::class.java)
                Result.Failure(Exception(" ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun walletChange(money: Int, type: String,bankAccountData: BankAccountData?=null): Result<NoDataResponse> {
        return try {
            val response = apiService.walletChange(
                TransactionRequest(money,type,bankAccountData?:BankAccountData("","",""))
            )
            if (response.isSuccessful) {
                Result.Success( response.body() ?: throw Exception("transaction failed"))

            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ApiError::class.java)
                Result.Failure(Exception(" ${errorResponse.message}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getUserTransaction(lastId:Int?=0,id:Int?): Result<ListTransactionResponse> {
        return try {
            if(id==null) return Result.Failure(Exception("id is null"))

            val response = apiService.getAllUserTransaction(id)
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