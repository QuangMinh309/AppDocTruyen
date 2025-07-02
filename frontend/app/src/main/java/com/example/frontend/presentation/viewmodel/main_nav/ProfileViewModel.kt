package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.NotificationRepository
import com.example.frontend.data.repository.ProfileRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val profileRepository: ProfileRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _readLists = MutableStateFlow<List<NameList>>(emptyList())
    val readLists: StateFlow<List<NameList>> = _readLists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _unReadNotificationsCount = MutableStateFlow(0)
    val unReadNotificationsCount: StateFlow<Int> = _unReadNotificationsCount.asStateFlow()

    init {
        loadUserData()
        loadReadLists()

        viewModelScope.launch {
            if(!notificationRepository.isConnected.value)
                notificationRepository.connect()

            try {
                val result = notificationRepository.getUnreadNotificationCount()
                _unReadNotificationsCount.value = when (result) {
                    is Result.Success -> result.data
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading user", result.exception)
                        0
                    }
                }
            } catch (e: Exception) {
                _unReadNotificationsCount.value =0
                Log.e("HomeViewModel", "Exception during load notificationscount", e)
            } finally {
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    val result = authRepository.getUserById(currentUser.id)
                    _user.value = when (result) {
                        is Result.Success -> result.data
                        is Result.Failure -> {
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

    fun loadReadLists() {
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

    fun updateReadList(nameListId: Int, name: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = profileRepository.updateNameList(nameListId, name, description)
                if (response is Result.Success) {
                    // Làm mới danh sách sau khi cập nhật
                    loadReadLists()
                } else {
                    println("Error updating read list: ${(response as Result.Failure).exception.message}")
                }
            } catch (e: Exception) {
                println("Exception during updateReadList: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteReadList(nameListId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = profileRepository.deleteNameList(nameListId)
                if (response is Result.Success) {
                    // Làm mới danh sách sau khi xóa
                    loadReadLists()
                } else {
                    println("Error deleting read list: ${(response as Result.Failure).exception.message}")
                }
            } catch (e: Exception) {
                println("Exception during deleteReadList: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


}