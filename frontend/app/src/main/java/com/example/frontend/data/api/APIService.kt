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
import com.example.frontend.data.model.User
import okhttp3.MultipartBody
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
    // API hiện hình ảnh
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>

    @GET("/api/images/{id}")    
    suspend fun getImageUrl(
        @Path("id") id: String
    ): Response<ImageUrlResponse>

    // API cho Category
    @GET("/api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/api/categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Int
    ): Response<Category>

    @POST("/api/categories")
    suspend fun createCategory(
        @Body category: Category
    ): Response<Category>

    @PUT("/api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body category: Category
    ): Response<Category>

    @DELETE("/api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Chapter
    @GET("/api/chapters")
    suspend fun getChapters(): Response<List<Chapter>>

    @GET("/api/chapters/{id}")
    suspend fun getChapterById(
        @Path("id") id: Int
    ): Response<Chapter>

    @POST("/api/chapters")
    suspend fun createChapter(
        @Body chapter: Chapter
    ): Response<Chapter>

    @PUT("/api/chapters/{id}")
    suspend fun updateChapter(
        @Path("id") id: Int,
        @Body chapter: Chapter
    ): Response<Chapter>

    @DELETE("/api/chapters/{id}")
    suspend fun deleteChapter(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Community
    @GET("/api/communities")
    suspend fun getCommunities(): Response<List<Community>>

    @GET("/api/communities/{id}")
    suspend fun getCommunityById(
        @Path("id") id: Int
    ): Response<Community>

    @POST("/api/communities")
    suspend fun createCommunity(
        @Body community: Community
    ): Response<Community>

    @PUT("/api/communities/{id}")
    suspend fun updateCommunity(
        @Path("id") id: Int,
        @Body community: Community
    ): Response<Community>

    @DELETE("/api/communities/{id}")
    suspend fun deleteCommunity(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Notification
    @GET("/api/notifications")
    suspend fun getNotifications(): Response<List<Notification>>

    @GET("/api/notifications/{id}")
    suspend fun getNotificationById(
        @Path("id") id: Int
    ): Response<Notification>

    @POST("/api/notifications")
    suspend fun createNotification(
        @Body notification: Notification
    ): Response<Notification>



    @DELETE("/api/notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho PasswordReset
    @GET("/api/password-resets")
    suspend fun getPasswordResets(): Response<List<Password_Reset>>

    @GET("/api/password-resets/{id}")
    suspend fun getPasswordResetById(
        @Path("id") id: Int
    ): Response<Password_Reset>

    @POST("/api/password-resets")
    suspend fun createPasswordReset(
        @Body passwordReset: Password_Reset
    ): Response<Password_Reset>

    @PUT("/api/password-resets/{id}")
    suspend fun updatePasswordReset(
        @Path("id") id: Int,
        @Body passwordReset: Password_Reset
    ): Response<Password_Reset>

    @DELETE("/api/password-resets/{id}")
    suspend fun deletePasswordReset(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Role

    @GET("/api/roles/{id}")
    suspend fun getRoleById(
        @Path("id") id: Int
    ): Response<Role>

    @POST("/api/roles")
    suspend fun createRole(
        @Body role: Role
    ): Response<Role>


    // API cho Story
    @GET("/api/stories")
    suspend fun getStories(): Response<List<Story>>

    @GET("/api/stories/{id}")
    suspend fun getStoryById(
        @Path("id") id: Int
    ): Response<Story>

    @GET("/api/stories/search")
    suspend fun searchStories(
        @Query("query") query: String
    ): Response<List<Story>>

    @POST("/api/stories")
    suspend fun createStory(
        @Body story: Story
    ): Response<Story>

    @PUT("/api/stories/{id}")
    suspend fun updateStory(
        @Path("id") id: Int,
        @Body story: Story
    ): Response<Story>

    @DELETE("/api/stories/{id}")
    suspend fun deleteStory(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Transaction
    @GET("/api/transactions")
    suspend fun getTransactions(): Response<List<Transaction>>

    @GET("/api/transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") id: Int
    ): Response<Transaction>

    @POST("/api/transactions")
    suspend fun createTransaction(
        @Body transaction: Transaction
    ): Response<Transaction>

    @PUT("/api/transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body transaction: Transaction
    ): Response<Transaction>

    @DELETE("/api/transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho User
    @GET("/api/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): Response<User>

    @POST("/api/users")
    suspend fun createUser(
        @Body user: User
    ): Response<User>

    @PUT("/api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: User
    ): Response<User>

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): Response<Unit>

    // API cho Functionality


    @GET("/api/functionalities/{id}")
    suspend fun getFunctionalityById(
        @Path("id") id: Int
    ): Response<Functionality>


    // API cho NameList
    @GET("/api/nameLists")
    suspend fun getNameLists(): Response<List<NameList>>

    @GET("/api/nameLists/{id}")
    suspend fun getNameListById(
        @Path("id") id: Int
    ): Response<NameList>

    @POST("/api/nameLists")
    suspend fun createNameList(
        @Body nameList: NameList
    ): Response<NameList>

    @PUT("/api/nameLists/{id}")
    suspend fun updateNameList(
        @Path("id") id: Int,
        @Body nameList: NameList
    ): Response<NameList>

    @DELETE("/api/nameLists/{id}")
    suspend fun deleteNameList(
        @Path("id") id: Int
    ): Response<Unit>

}

data class UploadResponse(
    val url: String, // URL ảnh từ Cloudinary
    val id: String   // ID ảnh (tên file trên Cloudinary)
)

data class ImageUrlResponse(
    val url: String
)