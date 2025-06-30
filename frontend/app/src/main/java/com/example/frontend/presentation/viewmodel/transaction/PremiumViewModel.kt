package com.example.frontend.presentation.viewmodel.transaction

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.UserRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val userRepository: UserRepository,
) : BaseViewModel(navigationManager) {
    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog
    fun setShowDialogState(value: Boolean){
        _isShowDialog.value=value
    }
    fun joinPremium() {
        viewModelScope.launch {
            try {
                val result = userRepository.purchasePremium()
                result.onSuccess {
                    _toast.value = "Purchase success.Wellcome to Premium!"
                }.onFailure { fail ->
                    _toast.value = fail.message
                }
                onGoBack()
            }catch (err:Exception){
                _toast.value = "Can not purchase premium !"
                Log.e("From VM Error","Error: ${err.message}")
            }
        }

    }

}
