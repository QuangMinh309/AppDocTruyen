package com.example.frontend.presentation.viewmodel.intro_authentification

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _mail = MutableStateFlow("")
    val mail: StateFlow<String> = _mail

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun onMailChange(mail: String) {
        _mail.value = mail
        _errorMessage.value = "" // Xóa thông báo lỗi khi thay đổi email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        _errorMessage.value = "" // Xóa thông báo lỗi khi thay đổi mật khẩu
    }

    fun checkingLogin(context: Context, rememberLogin: Boolean) {
        viewModelScope.launch {
            if (_mail.value.isEmpty() || _password.value.isEmpty()) {
                _errorMessage.value = "Please fill in all fields"
                Log.d("LoginViewModel", "Empty fields: mail=${_mail.value}, password=${_password.value}")
                return@launch
            }

            _isLoading.value = true
            try {
                val result = authRepository.login(_mail.value, _password.value, rememberLogin)
                result.onSuccess {
                    viewModelScope.launch {
                        _toast.value = it
                        val token = authRepository.getToken()
                        Log.d("LoginViewModel", "Token retrieved: $token")
                        navigationManager.navigate(Screen.MainNav.Home.route) {
                            popUpTo(Screen.Intro.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }.onFailure {
                    viewModelScope.launch {
                        _errorMessage.value = it.message ?: "Login failed"
                        Log.d("LoginViewModel", "Login failed: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                Log.e("LoginViewModel", "Network error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}