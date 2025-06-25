package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.ProfileRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _readLists = MutableStateFlow<List<NameList>>(emptyList())
    val readLists: StateFlow<List<NameList>> = _readLists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserData()
        loadReadLists()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    val result = authRepository.getUserById(currentUser.id)
                    _user.value = when (result) {
                        is Result.Success -> result.data
                        is Result.Failure -> {
                            // Log lỗi nhưng không làm gián đoạn UI
                            println("Error loading user: ${result.exception.message}")
                            null
                        }
                    }
                } else {
                    _user.value = null
                }
            } catch (e: Exception) {
                _user.value = null
                println("Exception during loadUserData: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadReadLists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = profileRepository.getReadLists()
                if (response is Result.Success) {
                    _readLists.value = response.data
                }
            } catch (e: Exception) {
                _readLists.value = emptyList()
                println("Error loading read lists: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}