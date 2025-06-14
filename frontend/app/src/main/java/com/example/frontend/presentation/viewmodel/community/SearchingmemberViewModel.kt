package com.example.frontend.presentation.viewmodel.community

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.demoAppUser
import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchingmemberViewModel @Inject constructor(
    navigationManager: NavigationManager,
    //savedStateHandle: SavedStateHandle,
) : BaseViewModel(navigationManager) {
    val memberList = listOf(demoUser, demoAppUser, demoUser, demoAppUser)
}