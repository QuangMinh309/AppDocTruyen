package com.example.frontend.presentation.viewmodel.transaction

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionAcceptViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    //    val depositMoney=savedStateHandle["depositMoney"]

}