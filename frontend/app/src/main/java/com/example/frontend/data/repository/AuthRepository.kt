package com.example.frontend.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.SendOTPResult
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.example.frontend.util.TokenManager
import com.example.frontend.util.UserPreferences
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val context: Context,
  //  private val gson: Gson
) {
    private var currentUser: User? = null

    suspend fun login(mail: String, password: String, rememberLogin: Boolean): Result<String> {
        return try {
            val response = apiService.login(ApiService.LoginRequest(mail, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.success == true) {
                    tokenManager.saveToken(loginResponse.data.accessToken)
                    if (rememberLogin) {
                        UserPreferences.saveUserData(context, mail, password, true)
                    } else {
                        UserPreferences.clearUserData(context)
                    }
                    currentUser = loginResponse.data.user
                    Result.Success("Login successful")
                } else {
                    Result.Failure(Exception("Invalid email or password"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrEmpty()) {
                    try {
                        val errorJson = JSONObject(errorBody)
                        errorJson.getString("message")
                    } catch (e: Exception) {
                        response.message()
                    }
                } else {
                    response.message()
                }
                Result.Failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun register(
        userName: String,
        email: String,
        dob: String,
        password: String,
        confirmPassword: String
    ): Result<String> {
        return try {
            val response = apiService.register(
                com.example.frontend.data.api.RegisterRequest(userName, email, dob, password, confirmPassword)
            )
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse?.success == true) {
                    Result.Success("Registration successful")
                } else {
                    Result.Failure(Exception(registerResponse?.message ?: "Unknown error"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrEmpty()) {
                    JSONObject(errorBody).getString("message")
                } else {
                    response.message()
                }
                Result.Failure(Exception("Registration failed: $message"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun sendOTP(email: String): Result<SendOTPResult> {
        return try {
            val response = apiService.sendOTP(com.example.frontend.data.api.EmailRequest(email))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(SendOTPResult(it.message, it.data?.userId))
                } ?: Result.Failure(Exception("No data in response"))
            } else {
                Result.Failure(Exception("Send OTP failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun verifyOTP(otp: String, userId: Int): Result<String> {
        return try {
            val response = apiService.verifyOTP(com.example.frontend.data.api.VerifyOTPRequest(otp.toIntOrNull() ?: 0, userId))
            if (response.isSuccessful) {
                Result.Success(response.body()?.message ?: "OTP verified successfully")
            } else {
                Result.Failure(Exception("OTP verification failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun resetPassword(
        otp: String,
        newPassword: String,
        confirmPassword: String,
        userId: Int
    ): Result<String> {
        return try {
            val response = apiService.resetPassword(
                com.example.frontend.data.api.ResetPasswordRequest(otp.toIntOrNull() ?: 0, newPassword, confirmPassword, userId)
            )
            if (response.isSuccessful) {
                Result.Success("Password reset successful")
            } else {
                Result.Failure(Exception("Reset password failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getToken(): String? = tokenManager.getToken()
    suspend fun clearToken() = tokenManager.clearToken()

    fun getCurrentUser(): User? = currentUser

    suspend fun updateUser(
        userId: Int,
        data: ApiService.UpdateUserRequest,
        avatarFile: MultipartBody.Part? = null,
        backgroundFile: MultipartBody.Part? = null
    ): Result<User> {
        return try {
            val dUserName: RequestBody? = data.dUserName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val dob: RequestBody? = data.DOB?.toRequestBody("text/plain".toMediaTypeOrNull())
            val userName: RequestBody? = data.userName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val mail: RequestBody? = data.mail?.toRequestBody("text/plain".toMediaTypeOrNull())
            val password: RequestBody? = data.password?.toRequestBody("text/plain".toMediaTypeOrNull())
            val about:RequestBody? = data.about?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.updateUser(
                userId,
                dUserName,
                dob,
                userName,
                mail,
                password,
                about ,
                avatarFile,
                backgroundFile
            )
            if (response.isSuccessful) {
                val updatedUser = response.body()?.data
                if (updatedUser != null) {
                    val refreshedUserResult = getUserById(userId)
                    if (refreshedUserResult is Result.Success) {
                        currentUser = refreshedUserResult.data
                        Result.Success(refreshedUserResult.data)
                    } else {
                        currentUser = updatedUser
                        Result.Success(updatedUser)
                    }
                } else {
                    Result.Failure(Exception("No user data in response"))
                }
            } else {
                Result.Failure(Exception("Failed to update user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    fun prepareImageFile(uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error preparing image file: ${e.message}", e)
            null
        }
    }

    suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val response = apiService.getUserById(userId)
            if (response.isSuccessful) {
                val user = response.body()?.data
                if (user != null) {
                    currentUser = user
                    Result.Success(user)
                } else {
                    Result.Failure(Exception("No user data in response"))
                }
            } else {
                Result.Failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String): Result<String> {
        return try {
            val userId = currentUser?.id ?: throw Exception("No current user found")
            val response = apiService.changePassword(
                userId,
                ApiService.ChangePasswordRequest(currentPassword, newPassword, confirmPassword)
            )
            if (response.isSuccessful) {
                Result.Success(response.body()?.message ?: "Đổi mật khẩu thành công")
            } else {
                Result.Failure(Exception("Failed to change password: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteUser(): Result<String> {
        return try {
            val userId = currentUser?.id ?: throw Exception("No current user found")
            val response = apiService.deleteUser(userId)
            if (response.isSuccessful) {
                Result.Success(response.body()?.message ?: "Xóa người dùng thành công")
            } else {
                Result.Failure(Exception("Failed to delete user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}