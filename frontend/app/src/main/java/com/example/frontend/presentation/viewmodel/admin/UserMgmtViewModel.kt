package com.example.frontend.presentation.viewmodel.admin

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.AdminRepository
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository
) : BaseViewModel(navigationManager) {
    private val _users = MutableStateFlow<List<User>>(emptyList())

    private val _displayedUsers = MutableStateFlow<List<User>>(emptyList())
    val displayedUsers : StateFlow<List<User>> = _displayedUsers

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser : StateFlow<User?> = _selectedUser

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _showOnlyLocked = MutableStateFlow(false)
    val showOnlyLocked: StateFlow<Boolean> = _showOnlyLocked

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init{
        loadUsers()
    }

    fun loadDisplayedUsers() {
        _displayedUsers.value = _users.value
        if(_showOnlyLocked.value == true)
            _displayedUsers.value = _displayedUsers.value.filter { it.status == "locked" }
        if(_userName.value != "")
            _displayedUsers.value = _displayedUsers.value.filter { it.name.contains(_userName.value) }
    }

    fun loadUsers()
    {
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val result = adminRepository.getAllUsers()
                result.onSuccess { list ->
                    _users.value = list
                    loadDisplayedUsers()
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun onUserNameChange(name : String)
    {
        _userName.value = name
        loadDisplayedUsers()
    }

    fun toggleShowOnlyLocked()
    {
        _showOnlyLocked.value = !_showOnlyLocked.value
        loadDisplayedUsers()
    }

    fun onSelectedUserChange(user : User)
    {
        _selectedUser.value = if(_selectedUser.value == user) null else user
    }

    fun lockSelectedUser()
    {
        if(_selectedUser.value == null) return
        if(_selectedUser.value!!.id == authRepository.getCurrentUser()?.id)
            _toast.value = "Bạn không thể khóa chính mình"
        else if(_selectedUser.value!!.status != "locked")
        {
            viewModelScope.launch {
                try {
                    val result = adminRepository.lockUser(_selectedUser.value!!.id)
                    result.onSuccess { message ->
                        _toast.value = message
                        loadUsers()
                        _selectedUser.value = null
                    }.onFailure { error ->
                        Log.e("apiError","Error: ${error.message}")
                    }
                }
                catch (e: Exception) {
                    _toast.value = "Error: ${e.message}"
                }
            }
        }
        else
        {
            viewModelScope.launch {
                try {
                    val result = adminRepository.unlockUser(_selectedUser.value!!.id)
                    result.onSuccess { message ->
                        _toast.value = message
                        loadUsers()
                        _selectedUser.value = null
                    }.onFailure { error ->
                        Log.e("apiError","Error: ${error.message}")
                    }
                } catch (e: Exception) {
                    _toast.value = "Error: ${e.message}"
                }
            }
        }
    }
}