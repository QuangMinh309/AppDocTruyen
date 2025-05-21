package com.example.frontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(protected val navigationManager: NavigationManager) : ViewModel() {

    protected val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> get() = _toast

    fun clearToast() {_toast.value = null}

    //region navigation fun
    fun onGoToProfileScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.MainNav.Profile.createRoute("1"))
        }
    }
    fun onGoToHomeScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.MainNav.Home.route)
        }
    }
    fun onGoToSearchScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.MainNav.Search.route)
        }
    }
    fun onGoToYourStoryScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.MainNav.YourStory.route)
        }
    }
    fun onGoToCommunityScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.MainNav.Community.route)
        }
    }
    fun onGoToSetting() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Setting.route)
        }
    }
    fun onGoToNotificationScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Notification.route)
        }
    }
    fun onGoToStoryScreen(id: Int) {
        viewModelScope.launch {
           navigationManager.navigate(Screen.Story.Detail.createRoute(id.toString()))
        }
    }
    fun onGoBack() {
        viewModelScope.launch {
            navigationManager.back()
        }
    }
    fun onGoToSearchingMemberScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.SearchingMember.createRoute(id.toString()))
        }
    }
    fun onGoToLoginScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.Login.route)
        }
    }
    fun onGoToRegisterScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.Register.route)
        }
    }
    //endregion

}