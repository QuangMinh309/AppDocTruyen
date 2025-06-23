package com.example.frontend.presentation.viewmodel.intro_authentification


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.RegisterRequest
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
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

    // Thêm trạng thái lỗi cho từng field
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
        _emailError.value = null // Xóa lỗi khi người dùng chỉnh sửa
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
            // Reset tất cả lỗi trước khi gửi request
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
                Log.d("RegisterViewModel", "Sending register request: userName=${_userName.value}, mail=${_email.value}")
                val response = apiService.register(
                    RegisterRequest(
                        userName = _userName.value,
                        mail = _email.value,
                        DOB = _dob.value.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        password = _password.value,
                        confirmPassword = _confirmPassword.value
                    )
                )
                Log.d("RegisterViewModel", "Response received: code=${response.code()}, body=${response.body()}")

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        _toast.value = "Sign Up success!"
                        Log.d("RegisterViewModel", "Registration successful")
                        onGoToLoginScreen()
                    } else {
                        _toast.value = "Registration failed: ${registerResponse?.message ?: "Unknown error"}"
                        Log.d("RegisterViewModel", "Registration failed: ${registerResponse?.message}")
                    }
                } else {
                    // Phân tích lỗi từ response body
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        val jsonError = JSONObject(errorBody)
                        val message = jsonError.getString("message")
                        _toast.value = "Registration failed: $message"

                        // Phân tích chi tiết lỗi
                        if (message.contains("Mật khẩu")) {
                            _passwordError.value = "Password must be at least 8 characters with uppercase, lowercase, and numbers"
                        }
                        if (message.contains("Ngày sinh")) {
                            _dobError.value = "Date of birth cannot be in the future"
                        }
                        // Thêm các điều kiện khác nếu cần
                    } else {
                        _toast.value = "Registration failed: ${response.message()}"
                    }
                    Log.d("RegisterViewModel", "Registration failed: $errorBody")
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("RegisterViewModel", "Network error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}