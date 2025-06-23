package com.example.frontend.presentation.viewmodel.intro_authentification

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
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onOTPChange(otp: String) {
        _otp.value = otp
    }

    fun sendOTPToEmail() {
        if (_email.value.isEmpty()) {
            _toast.value = "Vui lòng nhập email"
        } else {
            viewModelScope.launch {
                val result = authRepository.sendOTP(_email.value)
                result.onSuccess {
                    Log.d("ResetPasswordViewModel", "Send OTP Success: ${it.message}")
                    _toast.value = it.message
                    _userId.value = it.userId
                    Log.d("ResetPasswordViewModel", "UserId set to: ${_userId.value}")
                }.onFailure {
                    Log.e("ResetPasswordViewModel", "Send OTP Failure: ${it.message}")
                    _toast.value = "Gửi OTP thất bại: ${it.message}"
                }
            }
        }
    }

    fun checkOTP() {
        if (_otp.value.isEmpty()) {
            _toast.value = "Vui lòng nhập OTP"
        } else if (_userId.value == null) {
            _toast.value = "Vui lòng gửi OTP trước"
        } else {
            viewModelScope.launch {
                val result = authRepository.verifyOTP(_otp.value, _userId.value!!)
                result.onSuccess {
                    _toast.value = it
                    onGoToSetUpPassWordScreen(_otp.value, _userId.value!!)
                }.onFailure {
                    _toast.value = "OTP không đúng hoặc đã hết hạn"
                }
            }
        }
    }
}