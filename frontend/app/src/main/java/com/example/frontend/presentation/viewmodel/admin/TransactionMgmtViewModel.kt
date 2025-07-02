package com.example.frontend.presentation.viewmodel.admin

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.TransactionUpdateRequest
import com.example.frontend.data.repository.TransactionRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Transaction
import com.example.frontend.data.repository.AdminRepository

val dummyTypes: List<String> = listOf(
    "deposit",
    "withdraw",
    "purchase"
)

val dummyStatuses: List<String> = listOf(
    "pending",
    "success",
    "denied"
)

@HiltViewModel
class TransactionMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val transactionRepository : TransactionRepository,
    private val adminRepository: AdminRepository
) : BaseViewModel(navigationManager) {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _transactionId = MutableStateFlow("")
    val transactionId: StateFlow<String> = _transactionId

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType

    private val _selectedStatus = MutableStateFlow("")
    val selectedStatus: StateFlow<String> = _selectedStatus

    private var _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())

    private val _displayedTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val displayedTransactions: StateFlow<List<Transaction>> = _displayedTransactions

    private val _listTypes = MutableStateFlow<List<String>>(dummyTypes)
    val listTypes: StateFlow<List<String>> = _listTypes

    private val _statusTypes = MutableStateFlow<List<String>>(dummyStatuses)
    val statusTypes: StateFlow<List<String>> = _statusTypes

    private val _dialogContent = MutableStateFlow("")
    val dialogContent: StateFlow<String> = _dialogContent

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog: StateFlow<Boolean> = _isShowDialog

    val showDenyDialog = mutableStateOf(false)

    val showDeleteDialog = mutableStateOf(false)

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
                            listOf<Transaction>(result.data)
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

    fun onSelectTransaction(transaction: Transaction)
    {
        _selectedTransaction.value = if(_selectedTransaction.value == transaction) null else transaction
    }

    fun approveSelectedTransaction()
    {
        if(_selectedTransaction.value == null) return
        viewModelScope.launch {
            try {
                val result = adminRepository.approveTransaction(_selectedTransaction.value!!.transactionId, "success")
                result.onSuccess { message ->
                    _toast.value = message
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
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

    fun denySelectedTransaction()
    {
        if(_selectedTransaction.value == null) return
        viewModelScope.launch {
            try {
                val result = adminRepository.approveTransaction(_selectedTransaction.value!!.transactionId, "denied")
                result.onSuccess { message ->
                    _toast.value = message
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
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
                        _selectedTransaction.value
                    }
                }
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
    fun hideDenyConfirmation() {
        showDenyDialog.value = false
    }
    fun showDenyConfirmation() {
        showDenyDialog.value = true
    }

    fun hideDeleteConfirmation() {
        showDeleteDialog.value = false
    }
    fun showDeleteConfirmation() {
        showDeleteDialog.value = true
    }

    fun setShowDialogState(isShow: Boolean,content:String="") {
        _dialogContent.value = content
        _isShowDialog.value = isShow
    }
}