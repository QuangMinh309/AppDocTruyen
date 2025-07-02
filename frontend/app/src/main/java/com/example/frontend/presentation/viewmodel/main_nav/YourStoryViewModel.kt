package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.YourStoryRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourStoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val yourStoryRepository: YourStoryRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var lastId: Int? = null

    init {
        loadStories()
    }

    fun loadStories() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                val userId = currentUser.id
                val result = yourStoryRepository.getStoriesByUser(userId, lastId = lastId)
                when (result) {
                    is Result.Success -> {
                        val newStories = if (lastId == null) result.data else _stories.value + result.data
                        _stories.value = newStories
                        lastId = result.data.lastOrNull()?.id
                    }
                    is Result.Failure -> {
                        showToast("Failed to load stories: ${result.exception.message}")
                        Log.e("YourStoryViewModel", "Failed to load stories: ${result.exception.message}", result.exception)
                    }
                }
            }
            _isLoading.value = false
        }
    }



    fun deleteStory(storyId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = yourStoryRepository.deleteStory(storyId)
            when (result) {
                is Result.Success -> {
                    showToast("Story deleted successfully")
                    Log.d("YourStoryViewModel", "Deleted story: $storyId")
                    // Cập nhật danh sách truyện
                    _stories.value = _stories.value.filter { it.id != storyId }
                }
                is Result.Failure -> {
                    showToast("Failed to delete story: ${result.exception.message}")
                    Log.e("YourStoryViewModel", "Failed to delete story: ${result.exception.message}", result.exception)
                }
            }
            _isLoading.value = false
        }
    }


    fun loadMoreStories() {
        if (!isLoading.value) {
            loadStories()
        }
    }
}