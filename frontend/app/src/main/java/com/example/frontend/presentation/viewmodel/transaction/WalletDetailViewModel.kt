package com.example.frontend.presentation.viewmodel.transaction

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.User
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
class WalletDetailViewModel  @Inject constructor(
    navigationManager: NavigationManager,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel(navigationManager) {
    private val _user = MutableStateFlow<User?>(null)
    val user : StateFlow<User?> = _user

    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList : StateFlow<List<Transaction>> = _transactionList

    private val lastId = MutableStateFlow<Int?>(null)
    private val hasMore = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            loadUserData() // Tải dữ liệu người dùng từ API khi khởi tạo
            getUserTransaction()
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
                    Log.e("WalletDetailViewModel", "Failed to load user: ${result.exception.message}")
                }
            }
        }

    }
    private suspend fun getUserTransaction(){
        while(hasMore.value){
            when(val result= userRepository.getUserTransaction(lastId.value,user.value?.id)){
                is Result.Success -> {
                    lastId.value=result.data.nextLastId
                    hasMore.value=result.data.hasMore
                    _transactionList.value += result.data.transactions
                }
                is Result.Failure ->  Log.e("WalletDetailViewModel", "Failed to load user: ${result.exception.message}")
            }
        }

    }


}