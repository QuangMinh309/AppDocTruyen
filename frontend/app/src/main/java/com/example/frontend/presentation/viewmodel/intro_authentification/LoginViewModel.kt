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

    fun onMailChange(mail: String) {
        _mail.value = mail
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun checkingLogin(context: Context, rememberLogin: Boolean) {
        viewModelScope.launch {
            if (_mail.value.isEmpty() || _password.value.isEmpty()) {
                _toast.value = "Please fill in all fields"
                Log.d("LoginViewModel", "Empty fields: mail=${_mail.value}, password=${_password.value}")
                return@launch
            }

            _isLoading.value = true
            try {
                val result = authRepository.login(_mail.value, _password.value, rememberLogin)
                result.onSuccess {
                    viewModelScope.launch {
                        _toast.value = it
                        val token = authRepository.getToken() // Gọi getToken trong coroutine
                        Log.d("LoginViewModel", "Token retrieved: $token")
                        navigationManager.navigate(Screen.MainNav.Home.route) {
                            popUpTo(Screen.Intro.route) { inclusive = true } // Xóa Intro và Login
                            launchSingleTop = true // Tránh tạo mới HomeScreen
                        }
                    }
                }.onFailure {
                    viewModelScope.launch {
                        _toast.value = "Login failed: ${it.message}"
                        Log.d("LoginViewModel", "Login failed: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("LoginViewModel", "Network error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


}