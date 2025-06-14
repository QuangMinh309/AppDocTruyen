//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Result
//import com.example.frontend.data.model.User
//import com.example.frontend.data.model.NameList
//import javax.inject.Inject
//
//class NameListRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getNameLists(): Result<List<NameList>> {
//        return try {
//            val response = apiService.getNameLists()
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
//    suspend fun getNameListById(id: Int): Result<NameList> {
//        return try {
//            val response = apiService.getNameListById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("NameList not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createNameList(nameList: NameList): Result<NameList> {
//        return try {
//            val response = apiService.createNameList(nameList)
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
//    suspend fun updateNameList(id: Int, nameList: NameList): Result<NameList> {
//        return try {
//            val response = apiService.updateNameList(id, nameList)
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
//    suspend fun deleteNameList(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deleteNameList(id)
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