package com.example.frontend.presentation.viewmodel.transaction

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletDetailViewModel  @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager ) : BaseViewModel(navigationManager) {
    //val userID=savedStateHandle["id"]
        val user= demoUser


}