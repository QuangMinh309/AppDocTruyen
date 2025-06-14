package com.example.frontend.presentation.viewmodel

import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
}