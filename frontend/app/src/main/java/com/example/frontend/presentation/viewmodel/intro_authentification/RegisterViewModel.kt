package com.example.frontend.presentation.viewmodel.intro_authentification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _dob = MutableStateFlow(LocalDate.now())
    val dob: StateFlow<LocalDate> = _dob

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userNameError = MutableStateFlow<String?>(null)
    val userNameError: StateFlow<String?> = _userNameError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError

    private val _dobError = MutableStateFlow<String?>(null)
    val dobError: StateFlow<String?> = _dobError

    fun onEmailChange(email: String) {
        _email.value = email
        _emailError.value = null
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        _passwordError.value = null
    }

    fun onUserNameChange(userName: String) {
        _userName.value = userName
        _userNameError.value = null
    }

    fun onDOBChange(dob: LocalDate) {
        _dob.value = dob
        _dobError.value = null
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        _confirmPasswordError.value = null
    }

    fun checkRegister() {
        viewModelScope.launch {
            _userNameError.value = null
            _emailError.value = null
            _passwordError.value = null
            _confirmPasswordError.value = null
            _dobError.value = null

            if (_userName.value.isEmpty() || _email.value.isEmpty() ||
                _password.value.isEmpty() || _confirmPassword.value.isEmpty() || _dob.value.isEqual(LocalDate.now())
            ) {
                _toast.value = "Please fill all fields"
                if (_userName.value.isEmpty()) _userNameError.value = "Username is required"
                if (_email.value.isEmpty()) _emailError.value = "Email is required"
                if (_password.value.isEmpty()) _passwordError.value = "Password is required"
                if (_confirmPassword.value.isEmpty()) _confirmPasswordError.value = "Confirm password is required"
                if (_dob.value.isEqual(LocalDate.now())) _dobError.value = "Please select a valid date of birth"
                return@launch
            }

            if (_confirmPassword.value != _password.value) {
                _toast.value = "Password and confirm password do not match"
                _confirmPasswordError.value = "Passwords do not match"
                return@launch
            }

            _isLoading.value = true
            try {
                val result = authRepository.register(
                    _userName.value,
                    _email.value,
                    _dob.value.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    _password.value,
                    _confirmPassword.value
                )
                result.onSuccess {
                    _toast.value = it
                    onGoToLoginScreen()
                }.onFailure {
                    _toast.value = "Registration failed: ${it.message}"
                    if (it.message?.contains("Mật khẩu") == true) {
                        _passwordError.value = "Password must be at least 8 characters with uppercase, lowercase, and numbers"
                    }
                    if (it.message?.contains("Ngày sinh") == true) {
                        _dobError.value = "Date of birth cannot be in the future"
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


}