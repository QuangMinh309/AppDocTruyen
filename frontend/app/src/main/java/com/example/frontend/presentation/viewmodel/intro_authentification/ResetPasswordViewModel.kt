package com.example.frontend.presentation.viewmodel.intro_authentification

import android.util.Log
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager){
    private var sentOTP = "10"
    private val _email  = MutableStateFlow("")
    val email : StateFlow<String> = _email

    private var _otp = MutableStateFlow("")
    val otp : StateFlow<String> = _otp

    fun onEmailChange(email: String) {
        _email.value = email
    }
    fun onOTPChange(otp: String) {
        _otp.value = otp
    }

    fun sendOTPToEmail() {
        if(_email.value.isEmpty())
            _toast.value = "Please enter your email"
        else
            _toast.value = "OTP sent to your email. Please check your emailBox"
        Log.d("sentOTP", "sendOTPToEmail:${sentOTP}  ")

    }

    fun checkOTP() {
        if(sentOTP.isEmpty()){
            Log.d("sentOTPError", "sentOTP is empty ")
            return
        }
        if(_otp.value.isEmpty())
            _toast.value = "Please enter sent OTP"
        else{
            if(_otp.value == sentOTP)
                onGoToLoginScreen()
            else
                _toast.value = "OTP is incorrect"

        }

    }
}