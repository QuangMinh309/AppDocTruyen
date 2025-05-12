package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.ViewModel
import com.example.frontend.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    navigationManager: NavigationManager
) : ViewModel(){
    val commands = navigationManager.commands
}