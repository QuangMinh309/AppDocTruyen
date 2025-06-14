package com.example.frontend.presentation.viewmodel.admin

import androidx.core.text.isDigitsOnly
import com.example.frontend.data.model.Transaction
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

val dummyTransactions: List<Transaction> = listOf(
    Transaction(
        transactionId = 1,
        userId = 1,
        money = 100000,
        type = "deposit",
        time = LocalDate.now(),
        status = "pending",
        finishAt = LocalDate.now()
    ),
    Transaction(
        transactionId = 2,
        userId = 1,
        money = 20000,
        type = "withdraw",
        time = LocalDate.now(),
        status = "pending",
        finishAt = LocalDate.now()
    ),
    Transaction(
        transactionId = 3,
        userId = 1,
        money = 10000,
        type = "purchase",
        time = LocalDate.now(),
        status = "pending",
        finishAt = LocalDate.now()
    )
)

val dummyTypes: List<String> = listOf(
    "deposit",
    "withdraw",
    "purchase"
)

val dummyStatuses: List<String> = listOf(
    "pending",
    "success"
)

@HiltViewModel
class TransactionMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _transactionId = MutableStateFlow(0)
    val transactionId: StateFlow<Int> = _transactionId

    private val _selectedType = MutableStateFlow("")
    val selectedType : StateFlow<String> = _selectedType

    private val _selectedStatus = MutableStateFlow("")
    val selectedStatus : StateFlow<String> = _selectedStatus

    private val _transactions = MutableStateFlow<List<Transaction>>(dummyTransactions)
    val transactions : StateFlow<List<Transaction>> = _transactions

    private val _listTypes = MutableStateFlow<List<String>>(dummyTypes)
    val listTypes : StateFlow<List<String>> = _listTypes

    private val _statusTypes = MutableStateFlow<List<String>>(dummyStatuses)
    val statusTypes : StateFlow<List<String>> = _statusTypes

    fun onUserNameChange(name: String)
    {
        _userName.value = name
        _transactionId.value = 0
    }

    fun onTransactionIdChange(id: String)
    {
        if(!id.isDigitsOnly()) return
        val i = id.toIntOrNull()
        if(i != null) _transactionId.value = i
        _userName.value = ""
    }

    fun onSelectType(type: String)
    {
        _selectedType.value = (if(_selectedType.value == type) "" else type).toString()
    }

    fun onSelectStatus(type: String)
    {
        _selectedStatus.value = (if(_selectedStatus.value == type) "" else type).toString()
    }
}