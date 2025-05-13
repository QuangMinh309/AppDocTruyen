package com.example.frontend.presentation.viewmodel.intro_authentification

import androidx.lifecycle.viewModelScope
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    fun onGoToSignUpScreen(id: Int) {
        viewModelScope.launch {
            //navigationManager.navigate(Screen.introAuthentication.route)
        }
    }
}