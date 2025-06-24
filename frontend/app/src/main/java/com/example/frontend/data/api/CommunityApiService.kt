package com.example.frontend.data.api
import com.example.frontend.data.model.Community
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface CommunityApiService {
    @GET("api/communities/")
    suspend fun getAllCommunity(): Response<List<Community>>

    @PUT("api/communities/{id}")
    suspend fun updateCommunity(
        @Path("id") communityId: Int,
        @Body registerRequest: CommunityRequest): Response<Community>

    @PUT("api/communities/")
    suspend fun filterCommunity(@Body registerRequest: CommunityRequest): Response<List<Community>>


    @POST("api/communities/")
    suspend fun createCommunity(@Body registerRequest: CommunityRequest): Response<Community>

    @GET("api/communities/{id}")
    suspend fun getCommunityById(@Path("id") id: Int): Response<Community>

    @DELETE("api/communities/{id}")
    suspend fun deleteCommunity(@Path("id") id: Int): Response<Community>

}

data class CommunityRequest(
    @SerializedName("communityName") val name: String?=null,
    val categoryId: Int?=null,
    val avatarId:String?=null,
    @SerializedName("memberNum") val memberNum:Int?=null,
    val description:String?=null
)
