package com.example.frontend.presentation.viewmodel.intro_authentification

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager){

    private val _userName  = MutableStateFlow("")
    val userName : StateFlow<String> = _userName

    private val _email  = MutableStateFlow("")
    val email : StateFlow<String> = _email

    private val _password  = MutableStateFlow("")
    val password  : StateFlow<String> = _password

    private val _confirmPassword  = MutableStateFlow("")
    val confirmPassword  : StateFlow<String> = _confirmPassword

    private val _dob  = MutableStateFlow(LocalDate.now())
    val dob : StateFlow<LocalDate> = _dob
    fun onEmailChange(email: String) {
        _email.value = email
    }
    fun onPasswordChange(password: String)  {
        _password.value = password
    }
    fun onUserNameChange(userName: String)  {
        _userName.value = userName
    }
    fun onDOBChange(dob: LocalDate)  {
        _dob.value = dob
    }
    fun onConfirmPasswordChange(confirmPassword: String)  {
        _confirmPassword.value = confirmPassword
    }

    fun checkRegister() {
        if(_userName.value.isEmpty() || _email.value.isEmpty() ||
            _password.value.isEmpty() || _confirmPassword.value.isEmpty() || _dob.value.isEqual(LocalDate.now())) {
                _toast.value = "Please fill all fields"
                return
            }

        if(_confirmPassword.value != _password.value){
            _toast.value = "Password and confirm password do not match"
            return
        }
            _toast.value = "Sign Up success!"
            onGoToLoginScreen()


    }


}