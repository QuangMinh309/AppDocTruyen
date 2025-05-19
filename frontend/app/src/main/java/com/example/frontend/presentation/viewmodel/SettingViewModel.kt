package com.example.frontend.presentation.viewmodel

import com.example.frontend.navigation.NavigationManager
import javax.inject.Inject

class SettingViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
}