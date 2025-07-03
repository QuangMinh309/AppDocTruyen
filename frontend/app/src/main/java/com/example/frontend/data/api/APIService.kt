package com.example.frontend.data.api

import android.media.MediaDescription
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.DayRevenue
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.CreateStoryRepository
import com.example.frontend.presentation.viewmodel.transaction.BankAccountData
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
    @Multipart
    @POST("api/stories")
    suspend fun createStory(
        @Part("storyName") storyName: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("categories") categories: RequestBody?,
        @Part("pricePerChapter") pricePerChapter: RequestBody?,
        @Part coverImage: MultipartBody.Part?
    ): Response<StoryResponse>

    @GET("api/stories")
    suspend fun getAllStories(): Response<StoriesResponse>

    @GET("api/stories/updated")
    suspend fun getStoriesByUpdateDate(): Response<StoriesResponse>

    @GET("api/stories/vote")
    suspend fun getStoriesByVote(): Response<StoriesResponse>

    @GET("api/nameLists/user")
    suspend fun getUserReadingLists(): Response<NameListData>


    @GET("api/stories/{storyId}")
    suspend fun getStoryById(
        @Path("storyId") storyId:Int
    ): Response<StoryResponse>

    @DELETE("api/stories/{storyId}")
    suspend fun deleteStory(@Path("storyId") storyId:Int) : Response<StoryDeleteResponse>

    data class StoryResponse(
        val success: Boolean,
        val data: Story
    )

    data class StoryDeleteResponse(
        val success: Boolean,
        val message: String
    )



    data class NameListData(
        val readingLists: List<NameList>,
        val nextLastId: Int?,
        val hasMore: Boolean
    )

    @GET("api/stories/search")
    suspend fun searchStories(
        @Query("searchTerm") searchTerm: String,

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

       @Query("lastId") lastId: Int? = null
    ): Response<StoriesResponse>

    @GET("api/stories/category/{categoryId}")
    suspend fun getStoriesByCategory(
        @Path("categoryId") categoryId: Int
    ) : Response<CategoryStoriesResponse>

    @PUT("api/stories/{storyId}")
    suspend fun updateStatusStory(
        @Path("storyId") storyId: Int,
        @Body request: StoryUpdateRequest
    ): Response<UpdateStoryResponse>

    @Multipart
    @PUT("api/stories/{storyId}")
    suspend fun updateStory(
        @Path("storyId") storyId: Int,
        @Part("storyName") storyName: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("categories") categories: RequestBody?,
        @Part("pricePerChapter") pricePerChapter: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part coverImage: MultipartBody.Part?
    ): Response<UpdateStoryResponse>


    data class UpdateStoryRequest(
        val storyName: String?,
        val description: String?,
        val categories: List<Int>,
        val pricePerChapter: Float?
    )


    data class StoryUpdateRequest(
        val status: String
    )
    data class UpdateStoryResponse (
        val success: Boolean,
        val data: Story,
        val message: String
    )

    @GET("api/stories/{storyId}/vote/status")
    suspend fun checkVote(
        @Path("storyId") storyId:Int
    ):Response<CheckVoteResponse>

    data class CheckVoteResponse(
        val hasVoted: Boolean,
        val voteCount:Int
    )

    @POST("api/stories/{storyId}/vote")
    suspend fun voteStory(
        @Path("storyId") storyId:Int
    ): Response<VoteStoryResponse>

    data class VoteStoryResponse(
        val success: Boolean,
        val data: VoteStoryResponseData
    )

    data class VoteStoryResponseData(
        val message: String,
        val voteCount: Int,
        val hasVoted: Boolean

    )

    @GET("api/chapters/{chapterId}/read")
    suspend fun getChapter(
        @Path("chapterId") chapterId:Int
    ):Response<ChapterResponse>



    @GET("api/chapters/{chapterId}/read-next")
    suspend fun getNextChapter(
        @Path("chapterId") chapterId: Int
    ): Response<ChapterResponse>

    @POST("api/chapters/story/{storyId}")
    suspend fun createChapter(
        @Path("storyId") storyId: Int,
        @Body createChapterRequest: CreateChapterRequest
    ):Response<ChapterResponse>

    @DELETE("api/chapters/{chapterId}")
    suspend fun deleteChapter(
        @Path("chapterId") chapterId: Int,
    ): Response<DeleteChapterResponse>

    data class DeleteChapterResponse(
        val success: Boolean,
        val message: String
    )

    @PUT("api/chapters/story/{storyId}/{chapterId}")
    suspend fun updateChapter(
        @Path("storyId") storyId:Int,
        @Path("chapterId") chapterId:Int,
        @Body updateChapterRequest:UpdateChapterRequest
    ):Response<ChapterResponse>

    @POST("api/users/purchase-chapter/{chapterId}")
    suspend fun purchaseChapter(
        @Path("chapterId") chapterId:Int,
    ):Response<NoDataResponse>


    data class UpdateChapterRequest(
        val chapterName: String,
        val content:String
    )



    data class  CreateChapterRequest(
        val chapterName:String,
        val content:String
    )

    data class ChapterResponse(
        val success: Boolean,
        val data: Chapter
    )


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


        @POST("api/nameLists")
    suspend fun createNameList(
        @Body createNameListRequest:CreateNameListRequest
    ):Response<CreateNameListResponse>

    data class CreateNameListRequest(
        val nameList: String,
        val description: String
    )

    data class CreateNameListResponse(
        val nameListId: Int,
        val nameList: String,
        val userId: Int,
        val description: String
    )

    @PUT("api/nameLists/{nameListId}")
    suspend fun updateNameList(
        @Path("nameListId") nameListId: Int,
        @Body updateNameListRequest: UpdateNameListRequest
    ):Response<UpdateNameListReponse>

    data class UpdateNameListRequest(
        val nameList:String,
        val description: String
    )

    data class UpdateNameListReponse(
        val nameListId: Int,
        val nameList: String,
        val userId: Int,
        val description: String
    )

    @DELETE("api/nameLists/{nameListId}")
    suspend fun deleteNameList(
        @Path("nameListId") nameListId:Int
    ) :Response<DeleteNameListResponse>

    data class DeleteNameListResponse(
        val message: String
    )

    @GET("api/nameLists/{nameListsId}")
    suspend fun getNameListById (
        @Path("nameListsId") nameListsId:Int
    ): Response<NameListResponse>

    @DELETE("api/nameLists/{nameListId}/stories/{storyId}")
    suspend fun deleteStoryInNameList(
        @Path("nameListId") nameListId: Int,
        @Path("storyId") storyId:Int
    ):Response<DeleteStoryInNameListResponse>

    data class  DeleteStoryInNameListResponse(
        val storyId: Int,
        val nameListId: Int
    )

    @POST("api/nameLists/{nameListId}/stories")
    suspend fun addStoryToNameList(
        @Path ("nameListId") nameListId: Int,
        @Body addStoryToNameListRequest: AddStoryToNameListRequest
    ) :Response<AddStoryToNameListResponse>

    data class AddStoryToNameListRequest(
        val storyId: Int
    )

    data class AddStoryToNameListResponse(
        val storyId: Int,
        val nameListId: Int
    )

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


    @GET("api/users/follow/status/{userId}")
    suspend fun checkFollowUser(
        @Path("userId") userId:Int
    ):Response<CheckFollowUserResponse>

    data class CheckFollowUserResponse(
        val isFollowing: Boolean
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
        @Part("about") about: RequestBody?=null,
        @Part avatarFile: MultipartBody.Part? = null,
        @Part backgroundFile: MultipartBody.Part? = null
    ): Response<UpdateUserResponse>

    data class UpdateUserRequest(
        val dUserName: String? = null,
        val DOB: String? = null,
        val userName: String? = null,
        val mail: String? = null,
        val password: String? = null,
        val about: String?=null
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

    data class UsersResponse(
        val success: Boolean,
        val data: List<User>
    )

    // Thêm data class trong ApiService.kt
    data class ChangePasswordRequest(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String
    )

    // Thêm endpoint trong ApiService.kt
    @POST("api/users/change-password/{userId}")
    suspend fun changePassword(
        @Path("userId") userId: Int,
        @Body request: ChangePasswordRequest
    ): Response<NoDataResponse>

    @DELETE("api/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<NoDataResponse>


    @GET("api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @POST("api/categories/")
    suspend fun createCategory(@Body categoryName: CategoryRequest): Response<Category>

    @PUT("api/categories/{id}")
    suspend fun updateCategory(@Path("id") id: Int, @Body categoryName: CategoryRequest): Response<Category>

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<DeleteCategoryResponse>

    @GET("api/transactions/{transactionId}")
    suspend fun getTransactionById(@Path("transactionId") transactionId: Int): Response<Transaction>

    @GET("api/transactions/user/{userId}")
    suspend fun getUserTransactions(@Path("userId") userId: Int): Response<UserTransactionResponse>

    @PUT("api/transactions/{transactionId}")
    suspend fun updateTransaction(@Path("transactionId") transactionId: Int, @Body transaction: TransactionUpdateRequest): Response<Transaction>

    @DELETE("api/transactions/{transactionId}")
    suspend fun deleteTransaction(@Path("transactionId") transactionId: Int) : Response<Unit>

    @PUT("api/admins/{transactionId}/approve-trans")
    suspend fun approveTransaction(@Path("transactionId") transactionId: Int, @Body transaction: TransactionApproveRequest): Response<TransactionApproveResponse>

    @PUT("api/admins/{storyId}/approve")
    suspend fun approveStory(@Path("storyId") storyId: Int, @Body story: StoryApproveRequest): Response<StoryApproveResponse>

    @GET("api/admins")
    suspend fun getAllUsers(): Response<UsersResponse>

    @POST("api/admins/lock/{userId}")
    suspend fun lockUser(@Path("userId") userId: Int): Response<LockUserResponse>

    @POST("api/admins/unlock/{userId}")
    suspend fun unlockUser(@Path("userId") userId: Int): Response<LockUserResponse>

    @GET("api/admins/revenue/daily")
    suspend fun getReport(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<RevenueResponse>

    data class LockUserResponse(
        val success: Boolean,
        val message: String
    )

    data class RevenueResponse(
        val success: Boolean,
        val data: List<DayRevenue>
    )

    @GET("api/nameLists")
    suspend fun getNameLists(): Response<List<NameList>>

    @POST("api/users/follow")
    suspend fun follow(@Body followRequest: IdRequest): Response<NoDataResponse>
    @POST("api/users/unfollow")
    suspend fun unFollow(@Body unFollowRequest:IdRequest): Response<NoDataResponse>

    @POST("api/users/like-comment")
    suspend fun likeComment(
        @Body request:IdRequest
    ): Response<NoDataResponse>

    @POST("api/users/unlike-comment")
    suspend fun unlikeComment(
        @Body request:IdRequest
    ): Response<NoDataResponse>

    @POST("api/users/purchase-premium")
    suspend fun purchasePremium(): Response<NoDataResponse>

    @POST("api/users/report")
    suspend fun reportUser(@Body reportRequest: ReportRequest): Response<ReportResponse>


    @POST("api/users/wallet")
    suspend fun walletChange(@Body transactionRequest: TransactionRequest): Response<NoDataResponse>

    @GET("/api/transactions/user/{userId}")
    suspend fun getAllUserTransaction(
        @Path("userId") userId: Int,
    ): Response<ListTransactionResponse>



    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


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


    @POST("api/users/refresh-token")
    suspend fun refreshToken(
        @Body refreshToken: String
    ):Response<RefreshTokenResponse>

    data class RefreshTokenResponse(
        val success: Boolean,
        val message: String,
        val data : RefreshTokenData
    )

    data class RefreshTokenData(
        val accessToken: String
    )

    @POST("api/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/passwordResets/send-otp")
    suspend fun sendOTP(@Body emailRequest: EmailRequest): Response<SendOTPResponse>

    @POST("api/passwordResets/verify-otp")
    suspend fun verifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Response<VerifyOTPResponse>

    @POST("api/passwordResets/reset")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse>
}

class TransactionRequest (
    val money: Int,
    val type: String,
    val bankAccountData: BankAccountData?=null
)
class ListTransactionResponse(
    val transactions: List<Transaction>,
    val hasMore: Boolean,
    val nextLastId: Int?=-1
)


data class IdRequest (
    val id: Int
)

data class CategoryRequest(
    val categoryName: String
)

data class DeleteCategoryResponse(
    val status: Int,
    val message: String
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
    val transactions: List<Transaction>,
    val nextLastId: Int?,
    val hasMore: Boolean
)

data class TransactionUpdateRequest(
    val userId : Int,
    val money : Int,
    val type : String,
    val status : String
)

data class TransactionApproveRequest(
    val status : String
)

data class TransactionApproveResponse(
    val success : Boolean,
    val message : String
)

data class StoryApproveRequest(
    val status : String,
    val ageRange : String
)

data class StoryApproveResponse(
    val success : Boolean,
    val data : Story,
    val message: String
)

data class ReportRequest(
    val reportedUserId: Int,
    val reason: String
)

data class ReportResponse(
    val success: Boolean,
    val message: String,
    val data: ReportRequest? = null
)

data class ApiError(
    val status: Int,
    val message: String
)
//// Placeholder data classes (cần định nghĩa thêm nếu dùng)
//data class Story(val id: String) // Placeholder
//data class Category(val id: String) // Placeholder
//data class NameList(val id: String) // Placeholder