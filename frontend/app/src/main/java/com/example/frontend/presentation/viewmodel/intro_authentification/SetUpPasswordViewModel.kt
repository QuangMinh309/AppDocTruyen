package com.example.frontend.presentation.viewmodel.intro_authentification

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
import javax.inject.Inject

@HiltViewModel
class SetUpPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> = _userId

    fun setOTP(otp: String) {
        _otp.value = otp
    }

    fun setUserId(userId: Int) {
        _userId.value = userId
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun checkSetUpPassword() {
        if (_password.value.isEmpty() || _confirmPassword.value.isEmpty()) {
            _toast.value = "Vui lòng nhập đầy đủ mật khẩu"
            return
        }
        if (_password.value != _confirmPassword.value) {
            _toast.value = "Mật khẩu và xác nhận mật khẩu không khớp"
            return
        }
        if (_password.value.length < 8 || !isPasswordValid(_password.value)) {
            _toast.value = "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
            return
        }
        if (_otp.value.isEmpty()) {
            _toast.value = "Không tìm thấy OTP, vui lòng thử lại"
            return
        }
        if (_userId.value == 0) {
            _toast.value = "Không tìm thấy userId, vui lòng thử lại"
            return
        }

        viewModelScope.launch {
            val result = authRepository.resetPassword(_otp.value, _password.value, _confirmPassword.value, _userId.value)
            result.onSuccess {
                _toast.value = it
                onGoToLoginScreen()
            }.onFailure {
                _toast.value = "Đặt lại mật khẩu thất bại: ${it.message}"
            }
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