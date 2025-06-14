//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Category
//import com.example.frontend.data.model.Result
//import com.example.frontend.data.model.User
//import javax.inject.Inject
//
//class UserRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getUsers(): Result<List<User>> {
//        return try {
//            val response = apiService.getUsers()
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
//    suspend fun getUserById(id: Int): Result<User> {
//        return try {
//            val response = apiService.getUserById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("User not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createUser(user: User): Result<User> {
//        return try {
//            val response = apiService.createUser(user)
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
//    suspend fun updateUser(id: Int, user:User): Result<User> {
//        return try {
//            val response = apiService.updateUser(id, user)
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
//    suspend fun deleteUser(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deleteUser(id)
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
