package com.example.frontend.presentation.viewmodel.story

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService

import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.WriteRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val writeRepository: WriteRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    // State cho loading
    val isLoading = mutableStateOf(false)

    fun createChapter(chapterName: String, content: String) {
        viewModelScope.launch {
            isLoading.value = true
            val request = ApiService.CreateChapterRequest(chapterName, content)
            val result = writeRepository.createChapter(storyId.value, request)
            when (result) {
                is Result.Success -> {
                    _toast.value = "Chapter created successfully with ID: ${result.data.chapterId}"
                    onGoBack() // Quay lại màn hình trước đó sau khi tạo thành công
                }
                is Result.Failure -> {
                    _toast.value = "Failed to create chapter: ${result.exception.message}"
                }
            }
            isLoading.value = false
        }
    }
}