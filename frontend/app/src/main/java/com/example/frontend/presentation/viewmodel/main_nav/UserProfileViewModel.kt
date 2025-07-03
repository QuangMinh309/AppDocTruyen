package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.NotificationRepository
import com.example.frontend.data.repository.UserProfileRepository
import com.example.frontend.data.repository.UserRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel

import com.example.frontend.ui.screen.main_nav.demoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userRepository: UserRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _unReadNotificationsCount = MutableStateFlow(0)
    val unReadNotificationsCount: StateFlow<Int> = _unReadNotificationsCount.asStateFlow()
    private val _userId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("userId")))
    val userId: StateFlow<Int> = _userId.asStateFlow()

    private val _user = MutableStateFlow<User>(demoUser)
    val user: StateFlow<User> = _user.asStateFlow()

    private val _isFollowing = MutableStateFlow<Boolean?>(null)
    val isFollowing: StateFlow<Boolean?> = _isFollowing.asStateFlow()

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val Stories: StateFlow<List<Story>> = _stories.asStateFlow()

    val storyList = emptyList<Story>()

    val isLoading = mutableStateOf(false)
    val isLoadingStories = mutableStateOf(false)
    val isLoadingFollow = mutableStateOf(false) // Thêm biến để kiểm soát trạng thái loading của Follow/Unfollow

    init {
        viewModelScope.launch {
            isLoading.value = true
            loadUserProfile()
            checkFollowStatus()
            loadStories()

            isLoading.value = false
        }

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

    fun loadUserProfile() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = userProfileRepository.getUserById(userId.value)
                when (result) {
                    is Result.Success -> {
                        _user.value = result.data
                        Log.d("UserProfileViewModel", "Loaded user: ${result.data.name}")
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to load user: ${result.exception.message}"
                        Log.e("UserProfileViewModel", "Failed to load user: ${result.exception.message}", result.exception)
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error loading user: ${e.message}"
                Log.e("UserProfileViewModel", "Exception during loadUserProfile: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun checkFollowStatus() {
        viewModelScope.launch {
            try {
                val result = userProfileRepository.checkFollowUser(userId.value)
                when (result) {
                    is Result.Success -> {
                        _isFollowing.value = result.data
                        Log.d("UserProfileViewModel", "Follow status: ${result.data}")
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to check follow status: ${result.exception.message}"
                        _isFollowing.value = false
                        Log.e("UserProfileViewModel", "Failed to check follow status: ${result.exception.message}", result.exception)
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error checking follow status: ${e.message}"
                _isFollowing.value = false
                Log.e("UserProfileViewModel", "Exception during checkFollowStatus: ${e.message}", e)
            }
        }
    }

    fun loadStories(refresh: Boolean = false) {
        viewModelScope.launch {
            isLoadingStories.value = true
            try {
                if (refresh) {
                    _stories.value = emptyList()
                }
                val result = userProfileRepository.getStoriesByUser(userId.value)
                when (result) {
                    is Result.Success -> {
                        _stories.value = result.data
                        Log.d("UserProfileViewModel", "Loaded stories: ${result.data.size}")
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to load stories: ${result.exception.message}"
                        Log.e("UserProfileViewModel", "Failed to load stories: ${result.exception.message}", result.exception)
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error loading stories: ${e.message}"
                Log.e("UserProfileViewModel", "Exception during loadStories: ${e.message}", e)
            } finally {
                isLoadingStories.value = false
            }
        }
    }

    fun toggleFollow() {
        viewModelScope.launch {
            isLoadingFollow.value = true // Sử dụng isLoadingFollow thay vì isLoading
            try {
                if (_isFollowing.value == true) {
                    val result = userRepository.unFollow(user.value)
                    when (result) {
                        is Result.Success -> {
                            _isFollowing.value = false
                            _toast.value = result.data.message
                            Log.d("UserProfileViewModel", "Unfollowed user: ${userId.value}")
                        }
                        is Result.Failure -> {
                            _toast.value = "Failed to unfollow: ${result.exception.message}"
                            Log.e("UserProfileViewModel", "Failed to unfollow: ${result.exception.message}", result.exception)
                        }
                    }
                } else {
                    val result =  userRepository.follow(user.value)
                    when (result) {
                        is Result.Success -> {
                            _isFollowing.value = true
                            _toast.value = result.data.message
                            Log.d("UserProfileViewModel", "Followed user: ${userId.value}")
                        }
                        is Result.Failure -> {
                            _toast.value = "Failed to follow: ${result.exception.message}"
                            Log.e("UserProfileViewModel", "Failed to follow: ${result.exception.message}", result.exception)
                        }
                    }
                }
            } catch (e: Exception) {
                _toast.value = "Error toggling follow: ${e.message}"
                Log.e("UserProfileViewModel", "Exception during toggleFollow: ${e.message}", e)
            } finally {
                isLoadingFollow.value = false // Đặt lại isLoadingFollow
            }
        }
    }


}