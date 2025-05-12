package com.example.frontend.presentation.viewmodel.community

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager){
}