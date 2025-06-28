package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.TransactionUpdateRequest
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.Transaction2
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val apiService: ApiService
) {
//    suspend fun getTransactions(): Result<List<Transaction>> {
//        return try {
//            val response = apiService.getTransactions()
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: emptyList())
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }

    suspend fun getTransactionById(id: Int): Result<Transaction2> {
        return try {
            val response = apiService.getTransactionById(id)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Transaction not found"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getUserTransactions(id: Int): Result<List<Transaction2>> {
        return try {
            val response = apiService.getUserTransactions(id)
            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("No response body")
                Result.Success(body.transactions)
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

//    suspend fun createTransaction(transaction: Transaction): Result<Transaction> {
//        return try {
//            val response = apiService.createTransaction(transaction)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Creation failed"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }

    suspend fun updateTransaction(id: Int, transaction: TransactionUpdateRequest): Result<Transaction> {
        return try {
            val response = apiService.updateTransaction(id, transaction)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Update failed"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteTransaction(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteTransaction(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}