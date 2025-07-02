package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.NameListRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameListStoryViewModel @Inject constructor(
    private val nameListRepository: NameListRepository,
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _nameListsId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("nameListsId")))
    val nameListsId: StateFlow<Int> = _nameListsId.asStateFlow()

    private val _nameListName = MutableStateFlow<String>("")
    val nameListName: StateFlow<String> = _nameListName.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("nameListsId", 0).collect { newNameListId ->
                if (_nameListsId.value != newNameListId) {
                    _nameListsId.value = newNameListId
                    loadStories()
                }
            }
        }
        loadStories()
    }

    fun loadStories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = nameListRepository.getNameListById(nameListsId.value)
                _stories.value = when (result) {
                    is Result.Success -> {
                        val nameListName = result.data.nameList.name
                        _nameListName.value = nameListName
                        Log.d("NameListStoryViewModel", "Stories loaded: ${result.data.stories.size}, NameList: $nameListName")
                        result.data.stories
                    }
                    is Result.Failure -> {
                        Log.e("NameListStoryViewModel", "Error loading stories", result.exception)
                        showToast("Failed to load stories: ${result.exception.message}")
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _stories.value = emptyList()
                Log.e("NameListStoryViewModel", "Exception during loadStories", e)
                showToast("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteStoryInNameList(storyId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = nameListRepository.deleteStoryInNameList(nameListsId.value, storyId)
                when (result) {
                    is Result.Success -> {
                        Log.d("NameListStoryViewModel", "Deleted story $storyId from nameList ${nameListsId.value}")
                        showToast("Story removed successfully")
                        _stories.value = _stories.value.filter { it.id != storyId }
                    }
                    is Result.Failure -> {
                        Log.e("NameListStoryViewModel", "Failed to delete story $storyId: ${result.exception.message}", result.exception)
                        showToast("Failed to remove story: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("NameListStoryViewModel", "Exception during deleteStoryInNameList", e)
                showToast("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


}