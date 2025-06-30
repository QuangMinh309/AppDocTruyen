package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.CategoryStoryListRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryStoryListViewModel @Inject constructor(
    private val categoryStoryListRepository: CategoryStoryListRepository,
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _categoryStories = MutableStateFlow<List<Story>>(emptyList())
    val categoryStories: StateFlow<List<Story>> = _categoryStories.asStateFlow()

    private val _isCategoryStoriesLoading = MutableStateFlow(false)
    val isCategoryStoriesLoading: StateFlow<Boolean> = _isCategoryStoriesLoading.asStateFlow()

    private val _categoryId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("categoryId")))
    val categoryId: StateFlow<Int> = _categoryId.asStateFlow()

    private val _categoryName = MutableStateFlow(checkNotNull(savedStateHandle.get<String>("categoryName")))
    val categoryName: StateFlow<String> = _categoryName.asStateFlow()

    init {
        // Khởi tạo coroutine để thu thập thay đổi từ savedStateHandle
        viewModelScope.launch {
            savedStateHandle.getStateFlow("categoryId", 0).collect { newCategoryId ->
                if (_categoryId.value != newCategoryId) {
                    _categoryId.value = newCategoryId
                    loadStories()
                }
            }
        }
        viewModelScope.launch {
            savedStateHandle.getStateFlow("categoryName", "").collect { newCategoryName ->
                if (_categoryName.value != newCategoryName) {
                    _categoryName.value = newCategoryName
                }
            }
        }
        // Gọi loadStories lần đầu
        loadStories()
    }

    private fun loadStories() {
        viewModelScope.launch {
            _isCategoryStoriesLoading.value = true
            try {
                val categoryStoryListResult = categoryStoryListRepository.getStoriesByCategory(_categoryId.value)
                _categoryStories.value = when (categoryStoryListResult) {
                    is Result.Success -> {
                        categoryStoryListResult.data.forEach { story ->
                            if (story.name == null) Log.w("CategoryStoryListViewModel", "Story with id ${story.id} has null name")
                        }
                        Log.d("CategoryStoryListViewModel", "Stories loaded: ${categoryStoryListResult.data.size}")
                        categoryStoryListResult.data
                    }
                    is Result.Failure -> {
                        Log.e("CategoryStoryListViewModel", "Error loading stories", categoryStoryListResult.exception)
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _categoryStories.value = emptyList()
                Log.e("CategoryStoryListViewModel", "Exception during loadStories", e)
            } finally {
                _isCategoryStoriesLoading.value = false
            }
        }
    }
}