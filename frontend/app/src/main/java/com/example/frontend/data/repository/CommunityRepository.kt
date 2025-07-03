package com.example.frontend.data.repository

import com.example.frontend.data.api.CommunityApiService
import com.example.frontend.data.api.CommunityRequest
import com.example.frontend.data.api.DeleteCategoryResponse
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.google.gson.Gson
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val apiService: CommunityApiService,
) {
    suspend fun getAllCommunity(): Result<List<Community>> {
        return try {

            val response =  apiService.getAllCommunity()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Get all communities failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun filterCommunity(categoryId: Int,memberNum:Int=0): Result<List<Community>> {
        return try {
            val request = CommunityRequest(
                categoryId=categoryId,
                memberNum=memberNum
            )

            val response =  apiService.filterCommunity(request)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("filter communities failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getCommunityById(id: Int): Result<Community> {
        return try {
            val response = apiService.getCommunityById(id)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Community not found"))
            } else {
                Result.Failure(Exception("Get community failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun searchMembers(id: Int,name:String=""): Result<List<User>> {
        return try {
            val response = apiService.searchCommunityMembersByName(id,name)

            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Failure(Exception("Get community failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun createCommunity(community: CommunityRequest,imageInfo:Pair<String,String>?): Result<Community> {
        return try {
            val request = CommunityRequest(
                name=community.name,
                categoryId=community.categoryId,
                avatarId=imageInfo?.first,
                memberNum=community.memberNum,
                description=community.description
            )

            val response = apiService.createCommunity(request)
            val createdCommunity:Community? = response.body()?.copy()
            if (response.isSuccessful) {
                createdCommunity?.avatarUrl = imageInfo?.second?:""
                Result.Success( createdCommunity ?: throw Exception("Creation community failed"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateCommunity(community: Community,imageInfo:Pair<String,String>?): Result<Community> {
        return try {

            val request = CommunityRequest(
                name=community.name,
                categoryId=community.category.id,
                avatarId=imageInfo?.first,
                memberNum=community.memberNum,
                description=community.description
            )

            val response = apiService.updateCommunity(community.id,request)
            val updatedCommunity:Community? = response.body()?.copy()
            if (response.isSuccessful) {
                updatedCommunity?.avatarUrl = imageInfo?.second?:community.avatarUrl
                Result.Success( updatedCommunity?: throw Exception("Update community failed"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteCommunity(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteCommunity(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        Result.Failure(Exception(e)).toString()
                    }
                } else {
                    "Unknown error"
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}