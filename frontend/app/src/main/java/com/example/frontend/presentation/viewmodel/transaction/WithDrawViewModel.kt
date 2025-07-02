package com.example.frontend.presentation.viewmodel.transaction

import android.util.Log
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
class WithDrawViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _amountState = MutableStateFlow(-1L)
    val amountState : StateFlow<Long> = _amountState


    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user // Khởi tạo null, sẽ tải từ API

    private val _selectedOption = MutableStateFlow("full")
    val selectedOption : StateFlow<String> = _selectedOption

    private val _accountNumber = MutableStateFlow("")
    val accountNumber : StateFlow<String> = _accountNumber

    private val _accountHolderName = MutableStateFlow("")
    val accountHolderName : StateFlow<String> = _accountHolderName

    private val _bankName = MutableStateFlow("")
    val bankName : StateFlow<String> = _bankName

    fun changeAmount(newAmount: Long) {_amountState.value=newAmount}
    fun changeSelectedOption(newOption: String) {_selectedOption .value=newOption}
    fun changeAccountNumber(newAccountNumber: String) {_accountNumber.value=newAccountNumber}
    fun changeAccountHolderName(newAccountHolderName: String) {_accountHolderName.value=newAccountHolderName}
    fun changeBankName(newBankName: String) {_bankName.value=newBankName}

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

    fun withdraw() {
        viewModelScope.launch {
            try {
                val result = userRepository.walletChange(
                    amountState.value.toInt(),
                    "withdraw",
                    BankAccountData(accountNumber.value,accountHolderName.value,bankName.value))
                result.onSuccess { success ->
                    _toast.value = success.message
                    onGoToWalletDetailScreen()
                }.onFailure { fail ->
                    _toast.value =      fail.message
                }
            }catch (err:Exception){
                _toast.value = "Can not withdraw now.Please try again later!"
                Log.e("From VM Error","Error: ${err.message}")
            }
        }

    }

}
data class BankAccountData(
    val accountNumber: String,
    val accountHolder: String,
    val bankName: String
)