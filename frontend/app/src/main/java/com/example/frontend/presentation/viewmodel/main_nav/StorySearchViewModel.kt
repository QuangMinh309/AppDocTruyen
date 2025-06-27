package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.SearchStoryRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorySearchViewModel @Inject constructor(
    private val searchStoryRepository: SearchStoryRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedGenreTabIndex = MutableStateFlow(0)
    val selectedGenreTabIndex: StateFlow<Int> = _selectedGenreTabIndex

    private val _selectedStatusTabIndex = MutableStateFlow(0)
    val selectedStatusTabIndex: StateFlow<Int> = _selectedStatusTabIndex

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Load categories trước
            searchStoryRepository.getCategories().onSuccess { categories ->
                _categories.value = categories
                if (categories.isNotEmpty()) {
                    // Load stories với category đầu tiên và status mặc định
                    loadStories(categories[0].id, listOf("update", "full", "pending", "rejected","approved")[0])
                }
            }.onFailure { exception ->
                Log.e("StorySearchViewModel", "Error loading categories: ${exception.message}")
                _toast.value = "Failed to load categories"
            }
        }
    }

    private fun loadStories(categoryId: Int, status: String) {
        viewModelScope.launch {
            searchStoryRepository.getStoriesByCategoryAndStatus(categoryId, status).onSuccess { stories ->
                _stories.value = stories
            }.onFailure { exception ->
                Log.e("StorySearchViewModel", "Error loading stories: ${exception.message}")
                _toast.value = "Failed to load stories"
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                searchStoryRepository.searchStories(query).onSuccess { stories ->
                    _stories.value = stories
                }.onFailure { exception ->
                    Log.e("StorySearchViewModel", "Error searching stories: ${exception.message}")
                    _toast.value = "Search failed"
                }
            }
        } else {
            updateStories()
        }
    }

    fun onSelectedGenreTabIndexChange(index: Int) {
        _selectedGenreTabIndex.value = index
        updateStories()
    }

    fun onSelectedStatusTabIndexChange(index: Int) {
        _selectedStatusTabIndex.value = index
        updateStories()
    }

    private fun updateStories() {
        viewModelScope.launch {
            val categoryId = _categories.value.getOrNull(_selectedGenreTabIndex.value)?.id ?: 0
            val status = listOf("update", "full", "pending", "rejected","approved")[_selectedStatusTabIndex.value]
            loadStories(categoryId, status)
        }
    }
}