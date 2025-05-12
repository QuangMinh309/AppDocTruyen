package com.example.frontend.presentation.viewmodel.intro_authentification

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager){
}