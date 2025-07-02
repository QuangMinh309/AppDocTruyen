package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.repository.ReadRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readRepository: ReadRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _chapterId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("chapterId")))
    val chapterId: StateFlow<Int> = _chapterId.asStateFlow()

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    private val _isAuthor = MutableStateFlow(checkNotNull(savedStateHandle.get<Boolean>("isAuthor")))
    val isAuthor: StateFlow<Boolean> = _isAuthor.asStateFlow()

    // State để lưu chapter hiện tại
    private val _currentChapter = MutableStateFlow<Chapter?>(null)
    val currentChapter: StateFlow<Chapter?> = _currentChapter.asStateFlow()

    // State cho loading
    val isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            loadChapter()
        }
    }

    private suspend fun loadChapter() {
        isLoading.value = true
        val result = readRepository.getChapter(chapterId.value)
        when (result) {
            is Result.Success -> {
                _currentChapter.value = result.data
                Log.d("ReadViewModel", "Loaded chapter: ${result.data.chapterName}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to load chapter: ${result.exception.message}"
            }
        }
        isLoading.value = false
    }

    fun goToNextChapter() {
        viewModelScope.launch {
            isLoading.value = true
            val result = readRepository.getNextChapter(chapterId.value)
            when (result) {
                is Result.Success -> {
                    _currentChapter.value = result.data
                    _chapterId.value = result.data.chapterId // Cập nhật chapterId hiện tại
                    Log.d("ReadViewModel", "Loaded next chapter: ${result.data.chapterName}")
                }
                is Result.Failure -> {
                    _toast.value = "Failed to load next chapter: ${result.exception.message}"
                }
            }
            isLoading.value = false
        }
    }
}

