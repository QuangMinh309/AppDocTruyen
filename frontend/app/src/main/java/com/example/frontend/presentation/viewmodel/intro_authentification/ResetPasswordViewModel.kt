package com.example.frontend.presentation.viewmodel.intro_authentification

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.api.EmailRequest // Thêm import này
import com.example.frontend.data.api.VerifyOTPRequest // Thêm import này
import com.example.frontend.navigation.Screen
import android.util.Log
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val apiService: ApiService,
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
                try {
                    val response = apiService.sendOTP(EmailRequest(email = _email.value)) // Sửa ở đây
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _userId.value = it.data?.userId
                            _toast.value = it.message
                        }
                    } else {
                        _toast.value = "Gửi OTP thất bại: ${response.message()}"
                    }
                } catch (e: Exception) {
                    _toast.value = "Lỗi: ${e.message}"
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
                try {
                    val request = VerifyOTPRequest( // Sửa ở đây
                        OTP = _otp.value.toIntOrNull() ?: 0,
                        userId = _userId.value!!
                    )
                    val response = apiService.verifyOTP(request)
                    if (response.isSuccessful) {
                        _toast.value = response.body()?.message ?: "Xác minh OTP thành công"
                        onGoToSetUpPassWordScreen(_otp.value, _userId.value!!)
                    } else {
                        _toast.value = "OTP không đúng hoặc đã hết hạn"
                    }
                } catch (e: Exception) {
                    _toast.value = "Lỗi xác minh OTP: ${e.message}"
                }
            }
        }
    }

//    private fun onGoToSetUpPassWordScreen(otp: String, userId: Int) {
//        viewModelScope.launch {
//            navigationManager.navigate(Screen.Authentication.NewPassword.createRoute(otp, userId.toString()))
//        }
//    }
}