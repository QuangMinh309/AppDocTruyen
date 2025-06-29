package com.example.frontend.data.repository
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.User
import com.example.frontend.util.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val apiService: ApiService
)
{
    suspend fun getAllUsers() : Result<List<User>> {
        return try{
            val response = apiService.getAllUsers()
            if(response.isSuccessful) {
                val users = response.body()
                if(users?.success == true) {
                    Result.success(users.data)
                }
                else {
                    Result.failure(Exception("API returned success: false"))
                }
            }
            else {
                Result.failure(Exception("Failed to fetch users: ${response.message()}"))
            }
        }
        catch (e:Exception) {
            Result.failure(e)
        }
    }
    suspend fun lockUser(userId: Int) : Result<String>
    {
        return try{
            val response = apiService.lockUser(userId)
            if(response.isSuccessful) {
                val result = response.body()
                if(result?.success == true) {
                    Result.success(result.message)
                }
                else {
                    Result.failure(Exception("API returned success: false"))
                }
            }
            else {
                Result.failure(Exception("Failed to lock user: ${response.message()}"))
            }
        } catch (e:Exception) {
            Result.failure(e)
        }
    }
    suspend fun unlockUser(userId: Int) : Result<String>
    {
        return try{
            val response = apiService.unlockUser(userId)
            if(response.isSuccessful) {
                val result = response.body()
                if(result?.success == true) {
                    Result.success(result.message)
                }
                else {
                    Result.failure(Exception("API returned success: false"))
                }
            }
            else {
                Result.failure(Exception("Failed to unlock user: ${response.message()}"))
            }
        } catch (e:Exception) {
            Result.failure(e)
        }
    }
}