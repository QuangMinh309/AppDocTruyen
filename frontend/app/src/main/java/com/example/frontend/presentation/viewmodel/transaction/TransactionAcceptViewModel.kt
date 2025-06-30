package com.example.frontend.presentation.viewmodel.transaction

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.UserRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionAcceptViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    val amountState = savedStateHandle.getStateFlow("depositMoney", 0L)



    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user // Khởi tạo null, sẽ tải từ API

    init {
        viewModelScope.launch {
            loadUserData() // Tải dữ liệu người dùng từ API khi khởi tạo
        }
    }

    private suspend fun loadUserData() {

        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            when (val result = authRepository.getUserById(currentUser.id)) {
                is Result.Success -> {
                    _user.value = result.data
                }
                is Result.Failure -> {
                    Log.e("transactionViewModel", "Failed to load user: ${result.exception.message}")
                }


            }
        }

    }

    fun deposit() {
        viewModelScope.launch {
            try {
                val result = userRepository.walletChange(amountState.value.toInt(),"deposit")
                result.onSuccess { success ->
                    _toast.value = success.message
                    onGoToWalletDetailScreen()
                }.onFailure { fail ->
                    _toast.value = fail.message
                }
            }catch (err:Exception){
                _toast.value = "Can not deposit now.Please try again later!"
                Log.e("From VM Error","Error: ${err.message}")
            }
        }

    }

}