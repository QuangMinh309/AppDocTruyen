package com.example.frontend.data.repository

import android.content.Context
import android.util.Log
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.SendOTPResult
import com.example.frontend.data.model.Result
import com.example.frontend.util.TokenManager
import com.example.frontend.util.UserPreferences
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val context: Context
) {
    suspend fun login(mail: String, password: String, rememberLogin: Boolean): Result<String> {
        return try {
            val response = apiService.login(com.example.frontend.data.api.LoginRequest(mail, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.success == true) {
                    tokenManager.saveToken(loginResponse.data.accessToken)
                    if (rememberLogin) {
                        UserPreferences.saveUserData(context, mail, password, true)
                    } else {
                        UserPreferences.clearUserData(context)
                    }
                    Result.Success("Login successful")
                } else {
                    Result.Failure(Exception("Invalid email or password"))
                }
            } else {
                Result.Failure(Exception("Login failed: ${response.message()}"))
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
}