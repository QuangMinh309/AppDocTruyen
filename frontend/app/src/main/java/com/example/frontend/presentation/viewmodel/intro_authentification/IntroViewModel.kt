package com.example.frontend.presentation.viewmodel.intro_authentification

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
}