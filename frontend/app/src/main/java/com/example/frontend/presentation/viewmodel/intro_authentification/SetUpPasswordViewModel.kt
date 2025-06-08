package com.example.frontend.presentation.viewmodel.intro_authentification

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SetUpPasswordViewModel @Inject constructor(
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager){

    private val _password  = MutableStateFlow("")
    val password  : StateFlow<String> = _password

    private val _confirmPassword  = MutableStateFlow("")
    val confirmPassword  : StateFlow<String> = _confirmPassword

    fun onPasswordChange(password: String)  {
        _password.value = password
    }
    fun onConfirmPasswordChange(confirmPassword: String)  {
        _confirmPassword.value = confirmPassword
    }

    fun checkSetUpPassword() {
        if(_confirmPassword.value != _password.value){
            _toast.value = "Password and confirm password do not match"
            return
        }
        _toast.value = "Sign Up success!"
        onGoToLoginScreen()
    }


}