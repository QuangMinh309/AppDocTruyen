package com.example.frontend.data.util

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>

    @GET("/api/images/{id}")
    suspend fun getImageUrl(
        @Path("id") id: String
    ): Response<ImageUrlResponse>
}

data class UploadResponse(
    val url: String, // URL ảnh từ Cloudinary
    val id: String   // ID ảnh (tên file trên Cloudinary)
)

data class ImageUrlResponse(
    val url: String
)