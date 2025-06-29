package com.example.frontend.presentation.viewmodel.admin

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Role
import com.example.frontend.data.repository.AdminRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.frontend.data.model.Result

@HiltViewModel
class UserMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository
) : BaseViewModel(navigationManager) {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users : StateFlow<List<User>> = _users

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser : StateFlow<User?> = _selectedUser

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _showOnlySuspended = MutableStateFlow(false)
    val showOnlySuspended: StateFlow<Boolean> = _showOnlySuspended

    init{
        loadUsers()
    }

    fun loadUsers()
    {
        viewModelScope.launch {
            try{
                val result = adminRepository.getAllUsers()
                result.onSuccess { list ->
                    _users.value = list
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
        }
    }

    fun onUserNameChange(name : String)
    {
        _userName.value = name
        if(_userName.value != "")
        {
            viewModelScope.launch {
                try {

                }
                catch(e: Exception) {
                    _toast.value = "Error: ${e.message}"
                }
            }
        }
    }

    fun toggleShowOnlySuspended()
    {
        _showOnlySuspended.value = !_showOnlySuspended.value
    }

    fun onSelectedUserChange(user : User)
    {
        _selectedUser.value = if(_selectedUser.value == user) null else user
    }
}