package com.example.frontend.presentation.viewmodel.intro_authentification

import androidx.lifecycle.viewModelScope
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private val _email  = MutableStateFlow("")
    val email : StateFlow<String> = _email

    private val _password  = MutableStateFlow("")
    val password  : StateFlow<String> = _password


    fun onEmailChange(email: String) {
        _email.value = email
    }
    fun onPasswordChange(password: String)  {
        _password.value = password
    }

    fun onGoToResetPasswordScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.ResetPassword.route)
        }
    }

    fun checkingLogin() {
        if(_email.value.isEmpty() || _password.value.isEmpty()){
            _toast.value = "Please fill in all fields"
            return
        }
        if(_email.value == demoUser.mail && _password.value == "1234"){
            onGoToHomeScreen()
        }else{
            _toast.value = "Invalid email or password"
        }

    }
}