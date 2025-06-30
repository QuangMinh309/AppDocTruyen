package com.example.frontend.presentation.viewmodel.admin

import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager)  {

}