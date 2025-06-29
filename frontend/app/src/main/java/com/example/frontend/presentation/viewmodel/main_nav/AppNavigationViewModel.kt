package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.ViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    val navigationManager: NavigationManager
) : ViewModel(){
    val commands = navigationManager.commands
}