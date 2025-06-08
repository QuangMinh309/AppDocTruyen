package com.example.frontend.presentation.viewmodel.community

import androidx.lifecycle.viewModelScope
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.demoAppUser
import com.example.frontend.ui.screen.main_nav.demoCommunity
import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor( //savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    val communityId = 1
    val community = demoCommunity
    val memberList = listOf(demoUser, demoAppUser, demoUser, demoAppUser)


    fun onGoToChattingScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.Chat.createRoute(id.toString()))
        }
    }
}