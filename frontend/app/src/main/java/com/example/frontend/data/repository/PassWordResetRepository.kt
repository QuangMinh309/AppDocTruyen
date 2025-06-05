//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Category
//import com.example.frontend.data.model.Password_Reset
//import com.example.frontend.data.model.Result
//import javax.inject.Inject
//
//class PassWordResetRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getPassWordResets(): Result<List<Password_Reset>> {
//        return try {
//            val response = apiService.getPasswordResets()
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: emptyList())
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun getPassWordResetById(id: Int): Result<Password_Reset> {
//        return try {
//            val response = apiService.getPasswordResetById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("PassWordReset not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createPassWordReset(passwordReset: Password_Reset): Result<Password_Reset> {
//        return try {
//            val response = apiService.createPasswordReset(passwordReset)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Creation failed"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun updatePassWordReset(id: Int, passwordReset: Password_Reset): Result<Password_Reset> {
//        return try {
//            val response = apiService.updatePasswordReset(id, passwordReset)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Update failed"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun deletePassWordReset(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deletePasswordReset(id)
//            if (response.isSuccessful) {
//                Result.Success(Unit)
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//}