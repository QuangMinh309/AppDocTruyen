package com.example.frontend.presentation.viewmodel.intro_authentification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.LoginRequest
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.util.TokenManager
import com.example.frontend.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _mail = MutableStateFlow("")
    val mail: StateFlow<String> = _mail

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onMailChange(mail: String) {
        _mail.value = mail
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun checkingLogin(context: android.content.Context, rememberLogin: Boolean) {
        viewModelScope.launch {
            if (_mail.value.isEmpty() || _password.value.isEmpty()) {
                _toast.value = "Please fill in all fields"
                Log.d("LoginViewModel", "Empty fields: mail=${_mail.value}, password=${_password.value}")
                return@launch
            }

            _isLoading.value = true // Bật loading
            try {
                Log.d("LoginViewModel", "Sending login request: mail=${_mail.value}")
                val response = apiService.login(LoginRequest(_mail.value, _password.value))
                Log.d("LoginViewModel", "Response received: code=${response.code()}, body=${response.body()}")
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {
                        tokenManager.saveToken(loginResponse.data.accessToken)
                        if (rememberLogin) {
                            UserPreferences.saveUserData(context, _mail.value, _password.value, true)
                        } else {
                            UserPreferences.clearUserData(context)
                        }
                        _toast.value = "Login successful"
                        Log.d("LoginViewModel", "Login successful, token=${loginResponse.data.accessToken}")
                        onGoToHomeScreen()
                    } else {
                        _toast.value = "Invalid email or password"
                        Log.d("LoginViewModel", "Login failed: Invalid email or password")
                    }
                } else {
                    _toast.value = "Login failed: ${response.message()}"
                    Log.d("LoginViewModel", "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("LoginViewModel", "Network error: ${e.message}", e)
            } finally {
                _isLoading.value = false // Tắt loading
            }
        }
    }
}