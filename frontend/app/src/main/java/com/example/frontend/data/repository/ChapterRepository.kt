//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Category
//import com.example.frontend.data.model.Chapter
//import com.example.frontend.data.model.Result
//import javax.inject.Inject
//
//class ChapterRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getChapters(): Result<List<Chapter>> {
//        return try {
//            val response = apiService.getChapters()
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
//    suspend fun getChapterById(id: Int): Result<Chapter> {
//        return try {
//            val response = apiService.getChapterById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Chapter not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createChapter(chapter: Chapter): Result<Chapter> {
//        return try {
//            val response = apiService.createChapter(chapter)
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
//    suspend fun updateChapter(id: Int, chapter: Chapter): Result<Chapter> {
//        return try {
//            val response = apiService.updateChapter(id, chapter)
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
//    suspend fun deleteChapter(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deleteChapter(id)
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