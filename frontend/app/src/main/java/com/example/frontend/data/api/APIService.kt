package com.example.frontend.data.api

import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.Functionality
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Notification
import com.example.frontend.data.model.Password_Reset
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.Transaction2
import com.example.frontend.data.model.User
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getAllStories(): Response<StoriesResponse>

    @GET("api/stories/updated")
    suspend fun getStoriesByUpdateDate(): Response<StoriesResponse>

    @GET("api/stories/vote")
    suspend fun getStoriesByVote(): Response<StoriesResponse>

    @GET("api/nameLists/user")
    suspend fun getUserReadingLists(): Response<NameListData>



    data class NameListData(
        val readingLists: List<NameList>,
        val nextLastId: Int?,
        val hasMore: Boolean
    )

    @GET("api/stories/search")
    suspend fun searchStories(
        @Query("searchTerm") searchTerm: String,
        @Query("limit") limit: Int = 20,
        @Query("lastId") lastId: Int? = null
    ): Response<StoriesResponse>

    @GET("api/stories/category/{categoryId}/status/{status}")
    suspend fun getStoriesByCategoryAndStatus(
        @Path("categoryId") categoryId: Int,
        @Path("status") status: String
//        @Query("limit") limit: Int = 20,
//        @Query("lastId") lastId: Int? = null
    ): Response<StoriesResponse>

    @GET("api/stories/user/{userId}")
    suspend fun getStoriesByUser(
        @Path("userId") userId: Int,
        @Query("limit") limit: Int = 20,
       @Query("lastId") lastId: Int? = null
    ): Response<StoriesResponse>

    @GET("api/stories/category/{categoryId}")
    suspend fun getStoriesByCategory(
        @Path("categoryId") categoryId: Int
    ) : Response<CategoryStoriesResponse>

    data class CategoryStoriesResponse(
        val success: Boolean,
        val data: CategoryStoriesData
    )

    data class CategoryStoriesData(
        val formattedStories: List<Story>,
        val nextLastId: Int?,
        val hasMore: Boolean
    )

    data class StoriesResponse(
        val success: Boolean,
        val data: StoriesData
    )

    data class StoriesData(
        val stories: List<Story>,
        val nextLastId: Int,
        val hasMore: Boolean
    )

    @GET("api/nameLists/{nameListsId}")
    suspend fun getNameListById (
        @Path("nameListsId") nameListsId:Int
    ): Response<NameListResponse>

    data class NameListResponse(
       val nameList: NameListStory,
       val stories: List<Story>,
       val nextLastId: Int,
       val hasMore: Boolean
    )
    data class NameListStory(
        @SerializedName("nameListId") val id: Int,
        @SerializedName("nameList") val name: String,
        @SerializedName("userId") val userId: Int,
        @SerializedName("description") val description: String
    )

    @Multipart
    @PUT("api/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Part("dUserName") dUserName: RequestBody? = null,
        @Part("DOB") dob: RequestBody? = null,
        @Part("userName") userName: RequestBody? = null,
        @Part("mail") mail: RequestBody? = null,
        @Part("password") password: RequestBody? = null,
        @Part avatarFile: MultipartBody.Part? = null,
        @Part backgroundFile: MultipartBody.Part? = null
    ): Response<UpdateUserResponse>

    data class UpdateUserRequest(
        val dUserName: String? = null,
        val DOB: String? = null,
        val userName: String? = null,
        val mail: String? = null,
        val password: String? = null
    )

    data class UpdateUserResponse(
        val success: Boolean,
        val message: String,
        val data: User
    )

    @GET("api/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int
    ): Response<UserResponse>

    data class UserResponse(
        val success: Boolean,
        val data: User
    )


    @GET("api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @POST("api/categories/")
    suspend fun createCategory(@Body categoryName: CategoryRequest): Response<Category>

    @PUT("api/categories/{id}")
    suspend fun updateCategory(@Path("id") id: Int, @Body categoryName: CategoryRequest): Response<Category>

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<Unit>

    @GET("api/transactions/{transactionId}")
    suspend fun getTransactionById(@Path("transactionId") transactionId: Int): Response<Transaction2>

    @GET("api/transactions/user/{userId}")
    suspend fun getUserTransactions(@Path("userId") userId: Int): Response<UserTransactionResponse>

    @PUT("api/transactions/{transactionId}")
    suspend fun updateTransaction(@Path("transactionId") transactionId: Int, @Body transaction: TransactionUpdateRequest): Response<Transaction>

    @DELETE("api/transactions/{transactionId}")
    suspend fun deleteTransaction(@Path("transactionId") transactionId: Int) : Response<Unit>


    @GET("api/nameLists")
    suspend fun getNameLists(): Response<List<NameList>>

    @POST("api/users/follow")
    suspend fun follow(@Body followRequest: UserFollowRequest): Response<NoDataResponse>
    @POST("api/users/unfollow")
    suspend fun unFollow(@Body unFollowRequest: UserFollowRequest): Response<NoDataResponse>



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

data class UserFollowRequest (
    val followedId: Int
)

data class CategoryRequest(
    val categoryName: String
)

// Data classes cho password reset
data class SendOTPResult(val message: String, val userId: Int?)

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
data class NoDataResponse(
    val status: Int,
    val message: String
)

data class UserTransactionResponse(
    val transactions: List<Transaction2>,
    val nextLastId: Int?,
    val hasMore: Boolean
)

data class TransactionUpdateRequest(
    val userId : Int,
    val money : Int,
    val type : String,
    val status : String
)

//// Placeholder data classes (cần định nghĩa thêm nếu dùng)
//data class Story(val id: String) // Placeholder
//data class Category(val id: String) // Placeholder
//data class NameList(val id: String) // Placeholder