package com.example.frontend.presentation.viewmodel.intro_authentification

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onCurrentPasswordChange(currentPassword: String) {
        _currentPassword.value = currentPassword
    }

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun checkChangePassword() {
        if (_currentPassword.value.isEmpty() || _newPassword.value.isEmpty() || _confirmPassword.value.isEmpty()) {
            _toast.value = "Vui lòng nhập đầy đủ các trường mật khẩu"
            return
        }
        if (_newPassword.value == _currentPassword.value) {
            _toast.value = "Mật khẩu mới không được trùng với mật khẩu hiện tại"
            return
        }
        if (_newPassword.value != _confirmPassword.value) {
            _toast.value = "Mật khẩu mới và xác nhận mật khẩu không khớp"
            return
        }
        if (_newPassword.value.length < 8 || !isPasswordValid(_newPassword.value)) {
            _toast.value = "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.changePassword(_currentPassword.value, _newPassword.value, _confirmPassword.value)
            result.onSuccess {
                _toast.value = it // Sử dụng trực tiếp giá trị it (là String)
                onGoBack() // Quay lại SettingScreen sau khi đổi mật khẩu thành công
            }.onFailure {
                _toast.value = "Đổi mật khẩu thất bại: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val uppercaseRegex = "[A-Z]".toRegex()
        val lowercaseRegex = "[a-z]".toRegex()
        val numberRegex = "\\d".toRegex()
        val specialCharRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()
        return password.contains(uppercaseRegex) &&
                password.contains(lowercaseRegex) &&
                password.contains(numberRegex) &&
                password.contains(specialCharRegex)
    }
}