package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.repository.CreateStoryRepository
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
class CreateStoryViewModel @Inject constructor(
    private val createStoryRepository: CreateStoryRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _storyName = MutableStateFlow("")
    val storyName: StateFlow<String> = _storyName.asStateFlow()

    private val _description = MutableStateFlow<String?>(null)
    val description: StateFlow<String?> = _description.asStateFlow()

    private val _categories = MutableStateFlow<List<Int>>(emptyList())
    val categories: StateFlow<List<Int>> = _categories.asStateFlow()

    private val _pricePerChapter = MutableStateFlow<String>("")
    val pricePerChapter: StateFlow<String> = _pricePerChapter.asStateFlow()

    private val _coverImage = MutableStateFlow<File?>(null)
    val coverImage: StateFlow<File?> = _coverImage.asStateFlow()

    private val _availableCategories = MutableStateFlow<List<Category>>(emptyList())
    val availableCategories: StateFlow<List<Category>> = _availableCategories.asStateFlow()

    val isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

    private suspend fun loadCategories() {
        isLoading.value = true
        val result = createStoryRepository.getCategories()
        when (result) {
            is Result.Success -> {
                _availableCategories.value = result.data
                Log.d("CreateStoryViewModel", "Loaded ${result.data.size} categories")
            }
            is Result.Failure -> {
               showToast("Failed to load categories: ${result.exception.message}")
                Log.e("CreateStoryViewModel", "Failed to load categories: ${result.exception.message}")
            }
        }
        isLoading.value = false
    }

    fun updateStoryName(newName: String) {
        _storyName.value = newName
    }

    fun updateDescription(newDescription: String?) {
        _description.value = newDescription
    }

    fun updateCategories(newCategories: List<Int>) {
        _categories.value = newCategories
    }

    fun updatePricePerChapter(newPrice: String) {
        _pricePerChapter.value = newPrice
    }

    fun updateCoverImage(newImage: File?) {
        _coverImage.value = newImage
    }


    fun createStory() {
        viewModelScope.launch {
            if (_storyName.value.isEmpty()) {
                showToast("Please enter a story title")
                return@launch
            }
            if (_pricePerChapter.value.toFloatOrNull() == null) {
                _toast.value = "Please enter a valid price"
                return@launch
            }
            isLoading.value = true
            val result = createStoryRepository.createStory(
                storyName = _storyName.value,
                description = _description.value,
                categories = _categories.value,
                pricePerChapter = if(_pricePerChapter.value.isEmpty()) 0f else _pricePerChapter.value.toFloat(),
                coverImage = _coverImage.value
            )
            when (result) {
                is Result.Success -> {

                    showToast("Truyện của bạn đã được tạo. Vui lòng chờ Admin duyệt truyện ")
                    Log.d("CreateStoryViewModel", "Created story: ${result.data.name}")
                  onGoBack()
                }
                is Result.Failure -> {
                    showToast(" ${result.exception.message}")
                    Log.e("CreateStoryViewModel", "Failed to create story: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }
}