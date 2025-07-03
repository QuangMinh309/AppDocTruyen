package com.example.frontend.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.AddStoryToNameListRepository
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStoryToNameListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AddStoryToNameListRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _readingLists = MutableStateFlow<List<NameList>>(emptyList())
    val readingLists: StateFlow<List<NameList>> = _readingLists.asStateFlow()

    private val _storyId = MutableStateFlow<Int?>(null)
    val storyId: StateFlow<Int?> = _storyId.asStateFlow()

    val isLoading = mutableStateOf(false)
    val showCreateDialog = mutableStateOf(false)

    init {
        viewModelScope.launch {
            _storyId.value = savedStateHandle.get<Int>("storyId")
            if (_storyId.value == null) {
                showToast("Invalid story ID")
                onGoBack()
            } else {
                loadReadingLists()
            }
        }
    }

    private suspend fun loadReadingLists() {
        isLoading.value = true
        val result = repository.getUserReadingLists()
        when (result) {
            is Result.Success -> {
                _readingLists.value = result.data
                Log.d("AddStoryToNameListViewModel", "Loaded ${result.data.size} reading lists")
            }
            is Result.Failure -> {
                showToast("Failed to load reading lists: ${result.exception.message}")
                Log.e("AddStoryToNameListViewModel", "Failed to load reading lists: ${result.exception.message}")
            }
        }
        isLoading.value = false
    }

    fun addStoryToNameList(nameListId: Int) {
        viewModelScope.launch {
            val storyIdValue = _storyId.value ?: return@launch
            isLoading.value = true
            val result = repository.addStoryToNameList(nameListId, storyIdValue)
            when (result) {
                is Result.Success -> {
                    showToast("Story added to reading list successfully")
                    Log.d("AddStoryToNameListViewModel", "Added story $storyIdValue to nameList $nameListId")
                    onGoBack()
                }
                is Result.Failure -> {
                    showToast("${result.exception.message}")
                    Log.e("AddStoryToNameListViewModel", "Failed to add story: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }

    fun createNameList(nameList: String, description: String) {
        viewModelScope.launch {
            if (nameList.isBlank()) {
                showToast("Reading list name cannot be empty")
                return@launch
            }
            isLoading.value = true
            val result = repository.createNameList(nameList, description)
            when (result) {
                is Result.Success -> {
                    showToast("Reading list created successfully")
                    Log.d("AddStoryToNameListViewModel", "Created nameList: $nameList")
                    loadReadingLists() // Làm mới danh sách reading lists
                }
                is Result.Failure -> {
                    showToast("Failed to create reading list: ${result.exception.message}")
                    Log.e("AddStoryToNameListViewModel", "Failed to create nameList: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }


    fun showCreateDialog() {
        showCreateDialog.value = true
    }

    fun hideCreateDialog() {
        showCreateDialog.value = false
    }
}