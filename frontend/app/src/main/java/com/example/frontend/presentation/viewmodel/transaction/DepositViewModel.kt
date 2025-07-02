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

    private val _seletedTag = MutableStateFlow(-1)
    val selectedTag: StateFlow<Int> = _seletedTag

    fun changeAmount(amount: Long) {
        if (amount in Int.MIN_VALUE..Int.MAX_VALUE) {
            _amountState.value = amount
            _seletedTag.value=-1
        } else {
            _toast.value = "The amount is too big.Please enter another number."
        }

    }
    fun changeSelectedTag(index:Int) {_seletedTag.value = index}


}