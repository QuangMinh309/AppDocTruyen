//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Functionality
//import com.example.frontend.data.model.Result
//import com.example.frontend.data.model.User
//import javax.inject.Inject
//
//class FunctionalityRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getFunctionalityById(id: Int): Result<Functionality> {
//        return try {
//            val response = apiService.getFunctionalityById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("User not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//}