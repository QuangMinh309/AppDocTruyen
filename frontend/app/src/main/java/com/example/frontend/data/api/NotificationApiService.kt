package com.example.frontend.data.api
import com.example.frontend.data.model.Notification
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path



interface NotificationApiService {

    @GET("api/notifications/my-notifications")
    suspend fun getNotificationById(): Response<ResponseData>

    @GET("/api/notifications/unread-count")
    suspend fun getUnreadNotificationById(): Response<CountResponseData>

    @PUT("/api/notifications/{id}")
    suspend fun updateNotification(@Path("id") id: Int): Response<Notification>


    @DELETE("api/notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int): Response<NoDataResponse>

}
data class ResponseData(
    val success :Boolean,
    val data :List<Notification> = emptyList()

)
data class CountResponseData(
    val success :Boolean,
    val data :Int
)