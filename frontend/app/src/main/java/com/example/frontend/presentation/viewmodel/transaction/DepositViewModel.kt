package com.example.frontend.presentation.viewmodel.transaction

import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    private val _amountState = MutableStateFlow(0L)
    val amountState : StateFlow<Long> = _amountState

    private val _seletefTag = MutableStateFlow(-1)
    val selectedTag: StateFlow<Int> = _seletefTag

    fun changeAmount(amount: Long) {
        _amountState.value = amount
        _seletefTag.value=-1
    }
    fun changeSeletectedTag(index:Int) {_seletefTag.value = index}


}