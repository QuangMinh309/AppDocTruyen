package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.DeleteCategoryResponse
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
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

//    suspend fun getCategoryById(id: Int): Result<Category> {
//        return try {
//            val response = apiService.getCategoryById(id)
//            if (response.isSuccessful) {
//                Result.Success(response.body() ?: throw Exception("Category not found"))
//            } else {
//                Result.Failure(Exception("Failed with code: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.Failure(e)
//        }
//    }
//
    suspend fun createCategory(categoryName: String): Result<Category> {
        return try {
            val response = apiService.createCategory(com.example.frontend.data.api.CategoryRequest(categoryName))
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Creation failed"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        "Unknown error"
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

    suspend fun updateCategory(id: Int, categoryName: String): Result<Category> {
        return try {
            val response = apiService.updateCategory(id, com.example.frontend.data.api.CategoryRequest(categoryName))
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Update failed"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        "Unknown error"
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

    suspend fun deleteCategory(id: Int): Result<String> {
        return try {
            val response = apiService.deleteCategory(id)
            val res = response.body()
            if (response.isSuccessful) {
                Result.Success(res?.message ?: "Successfully deleted category")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, DeleteCategoryResponse::class.java)
                        errorResponse.message
                    } catch (e: Exception) {
                        "Unknown error"
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