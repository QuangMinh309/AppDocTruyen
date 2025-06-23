package com.example.frontend.data.api

import com.example.frontend.data.model.Category
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
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



    @GET("api/stories")
    suspend fun getStories(): Response<List<Story>>

    @GET("api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("api/nameLists")
    suspend fun getNameLists(): Response<List<NameList>>




    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/passwordResets/send-otp")
    suspend fun sendOTP(@Body emailRequest: EmailRequest): Response<SendOTPResponse>

    @POST("api/passwordResets/verify-otp")
    suspend fun verifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Response<VerifyOTPResponse>

    @POST("api/passwordResets/reset")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse>






}


// Data classes cho password reset
data class EmailRequest(
    val email: String
)

data class SendOTPResponse(
    val success: Boolean,
    val message: String,
    val data: SendOTPData? = null
)

data class SendOTPData(
    val userId: Int,
    val email: String
)

data class VerifyOTPRequest(
    val OTP: Int,
    val userId: Int
)

data class VerifyOTPResponse(
    val success: Boolean,
    val message: String,
    val data: VerifyOTPData? = null
)

data class VerifyOTPData(
    val userId: Int,
    val email: String
)

data class ResetPasswordRequest(
    val otp: Int,
    val newPassword: String,
    val confirmPassword: String,
    val userId: Int
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String,
    val data: ResetPasswordData? = null
)

data class ResetPasswordData(
    val userId: Int
)

// Data classes cho đăng nhập/đăng ký
data class RegisterRequest(
    val userName: String,
    val mail: String,
    val DOB: String,
    val password: String,
    val confirmPassword: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterData? = null
)

data class RegisterData(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)

data class LoginRequest(
    val mail: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)

data class User(
    val userId: Int,
    val userName: String,
    val roleId: Int,
    val dUserName: String,
    val mail: String,
    val about: String?,
    val followerNum: Int,
    val avatarId: String?,
    val backgroundId: String?,
    val wallet: String,
    val isPremium: Boolean,
    val status: String,
    val role: Role,
    val DOB: String
)

data class Role(
    val roleId: Int,
    val roleName: String
)

data class UploadResponse(
    val url: String,
    val publicId: String
)

data class ImageUrlResponse(
    val url: String,
)

//// Placeholder data classes (cần định nghĩa thêm nếu dùng)
//data class Story(val id: String) // Placeholder
//data class Category(val id: String) // Placeholder
//data class NameList(val id: String) // Placeholder