//package com.example.frontend.data.repository
//
//import com.example.frontend.data.api.ApiService
//import com.example.frontend.data.model.Community
//import com.example.frontend.data.model.Notification
//import com.example.frontend.data.model.Result
//import javax.inject.Inject
//
//class NotificationRepository @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getNotifications(): Result<List<Notification>> {
//        return try {
//            val response = apiService.getNotifications()
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
//    suspend fun getNotificationById(id: Int): Result<Notification> {
//        return try {
//            val response = apiService.getNotificationById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Notification not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
//    suspend fun createNotification(notification: Notification): Result<Notification> {
//        return try {
//            val response = apiService.createNotification(notification)
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
//
//    suspend fun deleteNotification(id: Int): Result<Unit> {
//        return try {
//            val response = apiService.deleteNotification(id)
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