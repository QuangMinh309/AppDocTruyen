package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.repository.UpdateChapterRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateChapterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateChapterRepository: UpdateChapterRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    private val _chapterId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("chapterId")))
    val chapterId: StateFlow<Int> = _chapterId.asStateFlow()

    // State cho loading
    val isLoading = mutableStateOf(false)

    // State cho chapter name và content
    private val _chapterName = MutableStateFlow("")
    val chapterName: StateFlow<String> = _chapterName.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    init {
        viewModelScope.launch {
            loadChapter()
        }
    }

    private suspend fun loadChapter() {
        isLoading.value = true
        val result = updateChapterRepository.getChapter(chapterId.value)
        when (result) {
            is Result.Success -> {
                _chapterName.value = result.data.chapterName
                _content.value = result.data.content ?: ""
                Log.d("UpdateChapterViewModel", "Loaded chapter: ${result.data.chapterName}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to load chapter: ${result.exception.message}"
                Log.e("UpdateChapterViewModel", "Failed to load chapter: ${result.exception.message}")
            }
        }
        isLoading.value = false
    }

    fun updateChapterName(newName: String) {
        _chapterName.value = newName
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }





    fun updateChapter(chapterName: String, content: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = updateChapterRepository.updateChapter(
                storyId = storyId.value,
                chapterId = chapterId.value,
                chapterName = chapterName,
                content = content
            )
            when (result) {
                is Result.Success -> {
                    _toast.value = "Chapter updated successfully"
                    Log.d("UpdateChapterViewModel", "Updated chapter: ${result.data.chapterName}")
                   onGoBack()
                }
                is Result.Failure -> {
                    _toast.value = "Failed to update chapter: ${result.exception.message}"
                    Log.e("UpdateChapterViewModel", "Failed to update chapter: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }
}