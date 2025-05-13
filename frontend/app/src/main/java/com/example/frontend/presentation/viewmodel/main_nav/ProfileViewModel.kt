package com.example.frontend.presentation.viewmodel.main_nav

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    //val userId: String =  savedStateHandle["id"] ?: "1"// checkNotNull(savedStateHandle["id"])
    val user = demoUser
    val storyList =  ExampleList

}