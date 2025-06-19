package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.HomeRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    private val _suggestedStories = MutableStateFlow<List<Story>>(emptyList())
    val suggestedStories: StateFlow<List<Story>> = _suggestedStories.asStateFlow()

    private val _newStories = MutableStateFlow<List<Story>>(emptyList())
    val newStories: StateFlow<List<Story>> = _newStories.asStateFlow()

    private val _topRankingStories = MutableStateFlow<List<Story>>(emptyList())
    val topRankingStories: StateFlow<List<Story>> = _topRankingStories.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _readLists = MutableStateFlow<List<NameList>>(emptyList())
    val readLists: StateFlow<List<NameList>> = _readLists.asStateFlow()

    private val _isSuggestedLoading = MutableStateFlow(false)
    val isSuggestedLoading: StateFlow<Boolean> = _isSuggestedLoading.asStateFlow()

    private val _isNewStoriesLoading = MutableStateFlow(false)
    val isNewStoriesLoading: StateFlow<Boolean> = _isNewStoriesLoading.asStateFlow()

    private val _isTopRankingLoading = MutableStateFlow(false)
    val isTopRankingLoading: StateFlow<Boolean> = _isTopRankingLoading.asStateFlow()

    private val _isCategoriesLoading = MutableStateFlow(false)
    val isCategoriesLoading: StateFlow<Boolean> = _isCategoriesLoading.asStateFlow()

    private val _isReadListsLoading = MutableStateFlow(false)
    val isReadListsLoading: StateFlow<Boolean> = _isReadListsLoading.asStateFlow()

    val userName: String
        get() = authRepository.getCurrentUser()?.dName ?: "User"

    init {
        loadStories()
        loadCategories()
        loadReadLists()
    }

    private fun loadStories() {
        viewModelScope.launch {
            _isSuggestedLoading.value = true
            _isNewStoriesLoading.value = true
            _isTopRankingLoading.value = true
            try {
                val suggestedResult = homeRepository.getAllStories()
                _suggestedStories.value = when (suggestedResult) {
                    is Result.Success -> {
                        suggestedResult.data.forEach { story ->
                            if (story.name == null) Log.w("HomeViewModel", "Story with id ${story.id} has null name")
                        }
                        Log.d("HomeViewModel", "Suggested stories: ${suggestedResult.data.size}")
                        suggestedResult.data
                    }
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading suggested stories", suggestedResult.exception)
                        emptyList()
                    }
                }

                val newStoriesResult = homeRepository.getStoriesByUpdateDate()
                _newStories.value = when (newStoriesResult) {
                    is Result.Success -> {
                        newStoriesResult.data.forEach { story ->
                            if (story.name == null) Log.w("HomeViewModel", "Story with id ${story.id} has null name")
                        }
                        Log.d("HomeViewModel", "Suggested stories: ${newStoriesResult.data.size}")
                        newStoriesResult.data
                    }
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading new stories", newStoriesResult.exception)
                        emptyList()
                    }
                }

                val topRankingResult = homeRepository.getStoriesByVote()
                _topRankingStories.value = when (topRankingResult) {
                    is Result.Success -> {
                        topRankingResult.data.forEach { story ->
                            if (story.name == null) Log.w("HomeViewModel", "Story with id ${story.id} has null name")
                        }
                        Log.d("HomeViewModel", "Suggested stories: ${topRankingResult.data.size}")
                        topRankingResult.data
                    }
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading top ranking stories", topRankingResult.exception)
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _suggestedStories.value = emptyList()
                _newStories.value = emptyList()
                _topRankingStories.value = emptyList()
                Log.e("HomeViewModel", "Exception during loadStories", e)
            } finally {
                _isSuggestedLoading.value = false
                _isNewStoriesLoading.value = false
                _isTopRankingLoading.value = false
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _isCategoriesLoading.value = true
            try {
                val response = homeRepository.getCategories()
                _categories.value = when (response) {
                    is Result.Success -> {
                        Log.d("HomeViewModel", "Categories: ${response.data.size}")
                        response.data
                    }
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading categories", response.exception)
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _categories.value = emptyList()
                Log.e("HomeViewModel", "Exception during loadCategories", e)
            } finally {
                _isCategoriesLoading.value = false
            }
        }
    }

    private fun loadReadLists() {
        viewModelScope.launch {
            _isReadListsLoading.value = true
            try {
                val response = homeRepository.getUserReadingLists()
                _readLists.value = when (response) {
                    is Result.Success -> {
                        Log.d("HomeViewModel", "Read lists: ${response.data.size}")
                        response.data
                    }
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading read lists", response.exception)
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _readLists.value = emptyList()
                Log.e("HomeViewModel", "Exception during loadReadLists", e)
            } finally {
                _isReadListsLoading.value = false
            }
        }
    }
}