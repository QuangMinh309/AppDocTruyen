package com.example.frontend.presentation.viewmodel.admin

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.TransactionUpdateRequest
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.model.Transaction2
import com.example.frontend.data.repository.TransactionRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.example.frontend.data.model.Result
import kotlinx.coroutines.flow.filter

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
class TransactionMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val transactionRepository : TransactionRepository
) : BaseViewModel(navigationManager) {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _transactionId = MutableStateFlow("")
    val transactionId: StateFlow<String> = _transactionId

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType

    private val _selectedStatus = MutableStateFlow("")
    val selectedStatus: StateFlow<String> = _selectedStatus

    private var _selectedTransaction = MutableStateFlow<Transaction2?>(null)
    val selectedTransaction: StateFlow<Transaction2?> = _selectedTransaction

    private val _transactions = MutableStateFlow<List<Transaction2>>(emptyList())
    val transactions: StateFlow<List<Transaction2>> = _transactions

    private val _displayedTransactions = MutableStateFlow<List<Transaction2>>(emptyList())
    val displayedTransactions: StateFlow<List<Transaction2>> = _displayedTransactions

    private val _listTypes = MutableStateFlow<List<String>>(dummyTypes)
    val listTypes: StateFlow<List<String>> = _listTypes

    private val _statusTypes = MutableStateFlow<List<String>>(dummyStatuses)
    val statusTypes: StateFlow<List<String>> = _statusTypes

    fun loadDisplayedTransactions() {
        if(_userId.value == "" && (_transactionId.value == "" || _transactionId.value == "0"))
        {
            _displayedTransactions.value = emptyList()
            return
        }
        _displayedTransactions.value = _transactions.value
        if(_selectedStatus.value != "") _displayedTransactions.value = _displayedTransactions.value.filter {
            it.status == _selectedStatus.value
        }
        if(_selectedType.value != "") _displayedTransactions.value = _displayedTransactions.value.filter {
            it.type == _selectedType.value
        }
        if(_selectedTransaction.value != null)
        {
            if((_selectedStatus.value != "" && _selectedTransaction.value!!.status != _selectedStatus.value)
                || (_selectedType.value != "" && _selectedTransaction.value!!.type != _selectedType.value)
                || (_transactionId.value != "" && _selectedTransaction.value!!.transactionId != _transactionId.value.toInt()))
                _selectedTransaction.value = null
        }
    }

    fun onUserNameChange(id: String)
    {
        _userId.value = id
        _transactionId.value = ""
        if(!id.isDigitsOnly())
        {
            _toast.value = "Invalid ID"
            return
        }
        val iD = id.toIntOrNull()
        if(iD != null)
        viewModelScope.launch {
            try {
                val result = transactionRepository.getUserTransactions(iD)
                _transactions.value = when(result) {
                    is Result.Success -> {
                        _toast.value = "Transactions loaded"
                        result.data
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to load transactions: ${result.exception.message}"
                        emptyList()
                    }
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStatus.value = ""
                _selectedType.value = ""
                loadDisplayedTransactions()
            }
        }
        else
        {
            loadDisplayedTransactions()
        }
    }

    fun onTransactionIdChange(id: String)
    {
        _transactionId.value = id
        _userId.value = ""
        if(!id.isDigitsOnly())
        {
            _toast.value = "Invalid ID"
            return
        }
        val iD = id.toIntOrNull()
        if(iD != null && iD > 0){
            viewModelScope.launch {
                try {
                    val result = transactionRepository.getTransactionById(iD)
                    _transactions.value = when(result) {
                        is Result.Success -> {
                            _toast.value = "Transaction loaded"
                            listOf<Transaction2>(result.data)
                        }
                        is Result.Failure -> {
                            _toast.value = "Failed to load transaction: ${result.exception.message}"
                            emptyList()
                        }
                    }
                }
                catch (e: Exception){
                    _toast.value = "Error: ${e.message}"
                }
                finally {
                    _selectedStatus.value = ""
                    _selectedType.value = ""
                    loadDisplayedTransactions()
                }
            }
        }
        else
        {
            loadDisplayedTransactions()
        }
    }

    fun onSelectType(type: String)
    {
        _selectedType.value = (if(_selectedType.value == type) "" else type).toString()
        loadDisplayedTransactions()
    }

    fun onSelectStatus(type: String)
    {
        _selectedStatus.value = (if(_selectedStatus.value == type) "" else type).toString()
        loadDisplayedTransactions()
    }

    fun onSelectTransaction(transaction: Transaction2)
    {
        _selectedTransaction.value = if(_selectedTransaction.value == transaction) null else transaction
    }

    fun updateSelectedTransaction(successSelected : Boolean)
    {
        if(_selectedTransaction.value == null) return
        viewModelScope.launch {
            try {
                val status = if(successSelected == true) "success" else "pending"
                val result = transactionRepository.updateTransaction(
                    _selectedTransaction.value!!.transactionId,
                    TransactionUpdateRequest(
                        _selectedTransaction.value!!.user.userId,
                        _selectedTransaction.value!!.money,
                        _selectedTransaction.value!!.type, status)
                )
                _selectedTransaction.value = when(result) {
                    is Result.Success -> {
                        _toast.value = "Transaction updated"
                        null
                    }

                    is Result.Failure -> {
                        _toast.value = "Failed to update transaction: ${result.exception.message}"
                        MutableStateFlow(_selectedTransaction.value)
                    }
                } as Transaction2?
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStatus.value = ""
                _selectedType.value = ""
                _selectedTransaction.value = null
                if(_userId.value != "") onUserNameChange(_userId.value)
                else if(_transactionId.value != "") onTransactionIdChange(_transactionId.value)
                loadDisplayedTransactions()
            }
        }
    }

    fun deleteSelectedTransaction()
    {
        if(_selectedTransaction.value == null) return
        viewModelScope.launch {
            try {
                val result = transactionRepository.deleteTransaction(_selectedTransaction.value!!.transactionId)
                _selectedTransaction.value = when(result) {
                    is Result.Success -> {
                        _toast.value = "${result.data}: ${_selectedTransaction.value!!.transactionId}"
                        null
                    }

                    is Result.Failure -> {
                        _toast.value = "Failed to delete transaction: ${result.exception.message}"
                        MutableStateFlow(_selectedTransaction.value)
                    }
                } as Transaction2?
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStatus.value = ""
                _selectedType.value = ""
                if(_userId.value != "") onUserNameChange(_userId.value)
                else if(_transactionId.value != "") onTransactionIdChange(_transactionId.value)
                loadDisplayedTransactions()
            }
        }
    }
}