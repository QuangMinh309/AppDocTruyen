package com.example.frontend.data.repository
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.StoryApproveRequest
import com.example.frontend.data.api.TransactionApproveRequest
import com.example.frontend.data.api.TransactionApproveResponse
import com.example.frontend.data.model.DayRevenue
import com.example.frontend.data.model.User
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
    suspend fun approveTransaction(transactionId: Int, status: String) : Result<String> {
        return try {
            val approvalBody = TransactionApproveRequest(status = status)
            val response = apiService.approveTransaction(transactionId, approvalBody)
            if(response.isSuccessful) {
                val result = response.body()
                if (result?.success == true) {
                    Result.success(result.message)
                } else {
                    Result.failure(Exception("API returned success: false"))
                }
            }
            else {
                Result.failure(Exception("Failed to approve transaction: ${response.message()}"))
            }
        } catch (e:Exception) {
            Result.failure(e)
        }
    }
    suspend fun approveStory(storyId: Int, age : String, status: String) : Result<String> {
        return try{
            val response = apiService.approveStory(storyId, StoryApproveRequest(status = status, ageRange = age))
            if(response.isSuccessful) {
                val result = response.body()
                if (result?.success == true) {
                    Result.success(result.message)
                } else {
                    Result.failure(Exception("API returned success: false"))
                }
            }
            else {
                Result.failure(Exception("Failed to approve story: ${response.message()}"))
            }
        } catch (e:Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReport(year: Int, month: Int) : Result<List<DayRevenue>> {
        return try {
            val response = apiService.getReport(year, month)
            if (response.isSuccessful) {
                val result = response.body()
                if (result?.success == true) {
                    Result.success(result.data)
                } else {
                    Result.failure(Exception("API returned success: false"))
                }
            } else {
                Result.failure(Exception("Failed to fetch report: ${response.message()}"))
            }
        }
        catch (e:Exception) {
            Result.failure(e)
        }
    }
}