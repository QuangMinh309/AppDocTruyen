package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.UpdateStoryRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateStoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateStoryRepository: UpdateStoryRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _storyId = MutableStateFlow<Int?>(null)
    val storyId: StateFlow<Int?> = _storyId.asStateFlow()

    private val _storyName = MutableStateFlow("")
    val storyName: StateFlow<String> = _storyName.asStateFlow()

    private val _description = MutableStateFlow<String?>(null)
    val description: StateFlow<String?> = _description.asStateFlow()

    private val _categories = MutableStateFlow<List<Int>>(emptyList())
    val categories: StateFlow<List<Int>> = _categories.asStateFlow()

    private val _pricePerChapter = MutableStateFlow("")
    val pricePerChapter: StateFlow<String> = _pricePerChapter.asStateFlow()

    private val _coverImage = MutableStateFlow<File?>(null)
    val coverImage: StateFlow<File?> = _coverImage.asStateFlow()

    private val _coverImgId = MutableStateFlow<String?>(null)
    val coverImgId: StateFlow<String?> = _coverImgId.asStateFlow()

    private val _coverImgUrl = MutableStateFlow<String?>(null)
    val coverImgUrl: StateFlow<String?> = _coverImgUrl.asStateFlow()

    private val _availableCategories = MutableStateFlow<List<Category>>(emptyList())
    val availableCategories: StateFlow<List<Category>> = _availableCategories.asStateFlow()

    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())
    val chapters: StateFlow<List<Chapter>> = _chapters.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val finalChapterId = mutableStateOf<Int?>(null)
    val isAuthor = mutableStateOf(true)
    val isLoading = mutableStateOf(false)

    init {
        val storyIdFromHandle = savedStateHandle.get<Int>("storyId")
        Log.d("UpdateStoryViewModel", "Initializing with storyId from SavedStateHandle: $storyIdFromHandle")
        if (storyIdFromHandle == null || storyIdFromHandle <= 0) {
            Log.e("UpdateStoryViewModel", "Invalid storyId: $storyIdFromHandle")
            showToast("Invalid story ID")
            _errorMessage.value = "Invalid story ID"
        }
        _storyId.value = storyIdFromHandle
        viewModelScope.launch {
            Log.d("UpdateStoryViewModel", "Starting init coroutine for storyId: $storyIdFromHandle")
            try {
                loadData()
            } catch (e: Exception) {
                Log.e("UpdateStoryViewModel", "Exception in init coroutine: ${e.message}", e)
                showToast("Error initializing data: ${e.message}")
                _errorMessage.value = "Error initializing data: ${e.message}"
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            Log.d("UpdateStoryViewModel", "Starting loadData for storyId: ${_storyId.value}")
            if (_storyId.value == null) {
                Log.e("UpdateStoryViewModel", "Cannot load data: storyId is null")
                showToast("Cannot load data: Invalid story ID")
                _errorMessage.value = "Cannot load data: Invalid story ID"
                return@launch
            }
            isLoading.value = true
            try {
                loadCategories()
                loadStoryDetails()
            } catch (e: Exception) {
                Log.e("UpdateStoryViewModel", "Error in loadData: ${e.message}", e)
                showToast("Error loading data: ${e.message}")
                _errorMessage.value = "Error loading data: ${e.message}"
            } finally {
                isLoading.value = false
                Log.d("UpdateStoryViewModel", "Finished loadData")
            }
        }
    }

    private suspend fun loadStoryDetails() {
        val storyId = _storyId.value ?: return
        Log.d("UpdateStoryViewModel", "Loading story details for storyId: $storyId")
        val result = updateStoryRepository.getStoryById(storyId)
        when (result) {
            is Result.Success -> {
                val story = result.data
                _storyName.value = story.name?.toString() ?: ""
                _description.value = story.description
                _categories.value = story.categories?.map { it.id } ?: emptyList()
                _pricePerChapter.value = story.pricePerChapter?.toString() ?: "0"
                _chapters.value = story.chapters ?: emptyList()
                _coverImgId.value = story.coverImgId
                _coverImgUrl.value = story.coverImgUrl
                finalChapterId.value = chapters.value.maxByOrNull { it.ordinalNumber }?.chapterId
                Log.d(
                    "UpdateStoryViewModel",
                    "Loaded story: name=${story.name}, " +
                            "description=${story.description}, categories=${story.categories?.map { it.id }}, " +
                            "pricePerChapter=${story.pricePerChapter}, chapters=${story.chapters?.size}, " +
                            "coverImgId=${story.coverImgId}, coverImgUrl=${story.coverImgUrl}"
                )
            }
            is Result.Failure -> {
                Log.e(
                    "UpdateStoryViewModel",
                    "Failed to load story: ${result.exception.message}",
                    result.exception
                )
                showToast("Failed to load story: ${result.exception.message}")
                _errorMessage.value = "Failed to load story: ${result.exception.message}"
            }
        }
    }

    private suspend fun loadCategories() {
        Log.d("UpdateStoryViewModel", "Loading categories")
        val result = updateStoryRepository.getCategories()
        when (result) {
            is Result.Success -> {
                _availableCategories.value = result.data
                Log.d(
                    "UpdateStoryViewModel",
                    "Loaded ${result.data.size} categories: ${result.data.map { it.name }}"
                )
            }
            is Result.Failure -> {
                Log.e(
                    "UpdateStoryViewModel",
                    "Failed to load categories: ${result.exception.message}",
                    result.exception
                )
                showToast("Failed to load categories: ${result.exception.message}")
                _errorMessage.value = "Failed to load categories: ${result.exception.message}"
            }
        }
    }

    fun updateStoryName(newName: String) {
        Log.d("UpdateStoryViewModel", "Updating storyName to: $newName")
        _storyName.value = newName
    }

    fun updateDescription(newDescription: String?) {
        Log.d("UpdateStoryViewModel", "Updating description to: $newDescription")
        _description.value = newDescription
    }

    fun updateCategories(newCategories: List<Int>) {
        Log.d("UpdateStoryViewModel", "Updating categories to: $newCategories")
        _categories.value = newCategories
    }

    fun updatePricePerChapter(newPrice: String) {
        Log.d("UpdateStoryViewModel", "Updating pricePerChapter to: $newPrice")
        _pricePerChapter.value = newPrice
    }

    fun updateCoverImage(newImage: File?) {
        Log.d("UpdateStoryViewModel", "Updating coverImage to: ${newImage?.absolutePath}")
        _coverImage.value = newImage
        if (newImage != null) {
            _coverImgId.value = null
            _coverImgUrl.value = null
        }
    }

    fun updateStory() {
        viewModelScope.launch {
            if (_storyName.value.isEmpty()) {
                Log.w("UpdateStoryViewModel", "Story name is empty")
                showToast("Please enter a story title")
                return@launch
            }
            val storyId = _storyId.value ?: return@launch
            Log.d(
                "UpdateStoryViewModel",
                "Updating story with: name=${_storyName.value}, " +
                        "description=${_description.value}, categories=${_categories.value}, " +
                        "pricePerChapter=${_pricePerChapter.value}, coverImage=${_coverImage.value?.absolutePath}"
            )
            isLoading.value = true
            val result = updateStoryRepository.updateStory(
                storyId = storyId,
                storyName = _storyName.value,
                description = _description.value,
                categories = _categories.value,
                pricePerChapter = _pricePerChapter.value.toFloatOrNull(),
                coverImage = _coverImage.value
            )
            when (result) {
                is Result.Success -> {
                    Log.d("UpdateStoryViewModel", "Updated story: ${result.data.name}")
                    _coverImgId.value = result.data.coverImgId
                    _coverImgUrl.value = result.data.coverImgUrl
                    showToast("Story updated successfully")
                    onGoBack()
                }
                is Result.Failure -> {
                    Log.e(
                        "UpdateStoryViewModel",
                        "Failed to update story: ${result.exception.message}",
                        result.exception
                    )
                    showToast("Failed to update story: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }

    fun deleteChapters(chapterIds: List<Int>) {
        viewModelScope.launch {
            Log.d("UpdateStoryViewModel", "Deleting chapters: $chapterIds")
            try {
                chapterIds.forEach { chapterId ->
                    val result = updateStoryRepository.deleteChapter(chapterId)
                    when (result) {
                        is Result.Success -> {
                            Log.d(
                                "UpdateStoryViewModel",
                                "Deleted chapter $chapterId: ${result.data.message}"
                            )
                            showToast(result.data.message)
                        }
                        is Result.Failure -> {
                            Log.e(
                                "UpdateStoryViewModel",
                                "Failed to delete chapter $chapterId: ${result.exception.message}"
                            )
                            showToast("Failed to delete chapter $chapterId: ${result.exception.message}")
                            throw result.exception
                        }
                    }
                }
                Log.d("UpdateStoryViewModel", "Refreshing story details after deleting chapters")
                loadStoryDetails()
            } catch (e: Exception) {
                Log.e("UpdateStoryViewModel", "Error deleting chapters: ${e.message}", e)
                showToast("Error deleting chapters: ${e.message}")
            }
        }
    }
}