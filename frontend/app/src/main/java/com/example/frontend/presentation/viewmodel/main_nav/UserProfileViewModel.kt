package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.UserProfileRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
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
    private val userProfileRepository: UserProfileRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _userId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("userId")))
    val userId: StateFlow<Int> = _userId.asStateFlow()

    private val _user = MutableStateFlow<User>(demoUser)
    val user: StateFlow<User> = _user.asStateFlow()

    private val _isFollowing = MutableStateFlow<Boolean?>(null)
    val isFollowing: StateFlow<Boolean?> = _isFollowing.asStateFlow()

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val Stories: StateFlow<List<Story>> = _stories.asStateFlow()

    val storyList = ExampleList

    val isLoading = mutableStateOf(false)
    val isLoadingStories = mutableStateOf(false)

    init {
        viewModelScope.launch {
            isLoading.value = true
            loadUserProfile()
            checkFollowStatus()
            loadStories()
            isLoading.value = false
        }
    }

    private suspend fun loadUserProfile() {
        val result = userProfileRepository.getUserById(userId.value)
        when (result) {
            is Result.Success -> {
                _user.value = result.data
                Log.d("UserProfileViewModel", "Loaded user: ${result.data.name}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to load user: ${result.exception.message}"
            }
        }
    }

    private suspend fun checkFollowStatus() {
        val result = userProfileRepository.checkFollowUser(userId.value)
        when (result) {
            is Result.Success -> {
                _isFollowing.value = result.data
                Log.d("UserProfileViewModel", "Follow status: ${result.data}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to check follow status: ${result.exception.message}"
                _isFollowing.value = false
            }
        }
    }

    private suspend fun loadStories() {
        isLoadingStories.value = true
        val result = userProfileRepository.getStoriesByUser(userId.value)
        when (result) {
            is Result.Success -> {
                _stories.value = result.data
                Log.d("UserProfileViewModel", "Loaded stories: ${result.data.size}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to load stories: ${result.exception.message}"
            }
        }
        isLoadingStories.value = false
    }

    fun toggleFollow() {
        viewModelScope.launch {
            isLoading.value = true
            if (_isFollowing.value == true) {
                val result = userProfileRepository.unFollowUser(userId.value)
                when (result) {
                    is Result.Success -> {
                        _isFollowing.value = false
                        _toast.value = result.data
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to unfollow: ${result.exception.message}"
                    }
                }
            } else {
                val result = userProfileRepository.followUser(userId.value)
                when (result) {
                    is Result.Success -> {
                        _isFollowing.value = true
                        _toast.value = result.data
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to follow: ${result.exception.message}"
                    }
                }
            }
            isLoading.value = false
        }
    }
}