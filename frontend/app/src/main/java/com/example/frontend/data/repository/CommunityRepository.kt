//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Chapter
//import com.example.frontend.data.model.Community
//import com.example.frontend.data.model.Result
//import javax.inject.Inject
//
//class CommunityRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getCommunities(): Result<List<Community>> {
//        return try {
//            val response = apiService.getCommunities()
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
//    suspend fun getCommunityById(id: Int): Result<Community> {
//        return try {
//            val response = apiService.getCommunityById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Community not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createCommunity(community: Community): Result<Community> {
//        return try {
//            val response = apiService.createCommunity(community)
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
//    suspend fun updateCommunity(id: Int, community: Community): Result<Community> {
//        return try {
//            val response = apiService.updateCommunity(id, community)
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
//    suspend fun deleteCommunity(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deleteCommunity(id)
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