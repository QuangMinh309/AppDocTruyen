package com.example.frontend.presentation.viewmodel.story

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.StoryDetailRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storyDetailRepository: StoryDetailRepository,
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()


    val story = mutableStateOf<Story?>(null)
    val isAuthor = mutableStateOf(false)
    val similarStories = mutableStateOf<List<Story>>(emptyList())
    val isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("storyId", 0).collect { newStoryId ->
                if (_storyId.value != newStoryId) {
                    _storyId.value = newStoryId
                    loadStoryDetails()
                    loadSimilarStories()
                }
            }
        }
        loadStoryDetails()
        loadSimilarStories()
    }


    private fun loadStoryDetails() {
        viewModelScope.launch {
            isLoading.value = true
            val result = storyDetailRepository.getStoryById(storyId.value)
            when (result) {
                is Result.Success -> {
                    story.value = result.data
                    checkIsAuthor()
                }
                is Result.Failure -> {
                    _toast.value = "Failed to load story: ${result.exception.message}"
                }
            }
            isLoading.value = false
        }
    }

    private fun loadSimilarStories() {
        viewModelScope.launch {
            val result = storyDetailRepository.getAllStories()
            when (result) {
                is Result.Success -> {
                    similarStories.value = result.data
                }
                is Result.Failure -> {
                    _toast.value = "Failed to load similar stories: ${result.exception.message}"
                }
            }
        }
    }

    private fun checkIsAuthor() {
        val currentUser = authRepository.getCurrentUser()
        story.value?.author?.id?.let { authorId ->
            isAuthor.value = currentUser?.id == authorId
        }
    }


}