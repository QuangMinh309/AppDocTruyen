package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Role
import javax.inject.Inject

class RoleRepository @Inject constructor(
    private val apiService: ApiService
) {


    suspend fun getRoleById(id: Int): Result<Role> {
        return try {
            val response = apiService.getRoleById(id)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Role not found"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun createRole(role: Role): Result<Role> {
        return try {
            val response = apiService.createRole(role)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: throw Exception("Creation failed"))
            } else {
                Result.Failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


}