package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.User
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
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                // Cập nhật novelsNum với dữ liệu mẫu (ví dụ: 5)
                val updatedUser = currentUser.copy(novelsNum = 5)
                _user.value = updatedUser
            }
            _isLoading.value = false
        }
    }

    private fun loadReadLists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = profileRepository.getReadLists()
                if (response is com.example.frontend.data.model.Result.Success) {
                    _readLists.value = response.data
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            } finally {
                _isLoading.value = false
            }
        }
    }


}