package com.example.frontend.presentation.viewmodel.admin

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.AdminRepository
import com.example.frontend.data.repository.CategoryRepository
import com.example.frontend.data.repository.HomeRepository
import com.example.frontend.data.repository.StoryDetailRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val homeRepository: HomeRepository,
    private val adminRepository: AdminRepository,
    private val categoryRepository: CategoryRepository,
    private val storyDetailRepository: StoryDetailRepository
) : BaseViewModel(navigationManager) {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories : StateFlow<List<Category>> = _categories

    private val _selectedStory = MutableStateFlow<Story?>(null)
    val selectedStory : MutableStateFlow<Story?> = _selectedStory

    private val _selectedSearchType = MutableStateFlow<String?>("Name")
    val selectedSearchType : MutableStateFlow<String?> = _selectedSearchType

    private val _selectedStates = MutableStateFlow<List<String>>(emptyList())
    val selectedStates : MutableStateFlow<List<String>> = _selectedStates

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories : MutableStateFlow<List<String>> = _selectedCategories

    private val _tbSearchValue = MutableStateFlow("")
    val tbSearchValue : MutableStateFlow<String> = _tbSearchValue

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories : MutableStateFlow<List<Story>> = _stories

    private val _displayedStories = MutableStateFlow<List<Story>>(emptyList())
    val displayedStories : MutableStateFlow<List<Story>> = _displayedStories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog

    init{
        loadStories()
        loadCategories()
    }

    fun loadStories()
    {
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val result = homeRepository.getAllStories()
                _stories.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    is Result.Failure -> {
                        Log.e("StoryMgmtViewModel", "Error loading suggested stories", result.exception)
                        _toast.value = "Failed to load stories: ${result.exception.message}"
                        emptyList()
                    }
                }
                loadDisplayedStories()
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    private fun loadCategories()
    {
        viewModelScope.launch {
            try {
                val result = categoryRepository.getCategories()
                _categories.value = when (result)
                {
                    is Result.Success -> result.data
                    is Result.Failure -> {
                        _toast.value = "Failed to load categories: ${result.exception.message}"
                        emptyList()
                    }
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
        }
    }

    fun loadDisplayedStories()
    {
        _displayedStories.value = _stories.value
        if(_selectedSearchType.value != "")
        {
            if (_selectedSearchType.value == "Author") {
                _displayedStories.value = _displayedStories.value.filter {
                    it.author.dName?.contains(_tbSearchValue.value, ignoreCase = true) == true
                }
            } else {
                _displayedStories.value = _displayedStories.value.filter {
                    it.name?.contains(_tbSearchValue.value, ignoreCase = true) == true
                }
            }
        }
        if(!_selectedStates.value.isEmpty())
        {
            _displayedStories.value = _displayedStories.value.filter {
                _selectedStates.value.contains(it.status)
            }
        }
        if(!_selectedCategories.value.isEmpty())
        {
            _displayedStories.value = _displayedStories.value.filter { story ->
                story.categories!!.any { category ->
                    _selectedCategories.value.contains(category.name)
                }
            }
        }
        _selectedStory.value = null
    }

    fun onSelectSearchType(type: String)
    {
        _selectedSearchType.value = type
    }

    fun onSearch(name : String)
    {
        _tbSearchValue.value = name
        loadDisplayedStories()
    }

    fun onSelectState(state: String)
    {
        if (_selectedStates.value.contains(state))
        {
            _selectedStates.value = _selectedStates.value.filter { it != state }
        }
        else
            _selectedStates.value = _selectedStates.value + state
    }

    fun onSelectCategory(category: String)
    {
        if (_selectedCategories.value.contains(category))
        {
            _selectedCategories.value = _selectedCategories.value.filter { it != category }
        }
        else
            _selectedCategories.value = _selectedCategories.value + category
    }

    fun onSelectStory(story : Story)
    {
        if(_selectedStory.value == story) _selectedStory.value = null else _selectedStory.value = story
    }

    fun approveSelectedStory(age: String)
    {
        if(_selectedStory.value == null) return
        if(age.isEmpty() || !age.isDigitsOnly() || age.toInt() < 0) return
        viewModelScope.launch {
            try {
                val result = adminRepository.approveStory(_selectedStory.value!!.id, age, "approved")
                result.onSuccess { message ->
                    _toast.value = message
                }.onFailure { error ->
                    Log.e("apiError", "Error: ${error.message}")
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStory.value = null
                loadStories()
            }
        }
    }

    fun rejectSelectedStory(age: String)
    {
        if(_selectedStory.value == null) return
        if(age.isEmpty() || !age.isDigitsOnly() || age.toInt() < 0) return
        viewModelScope.launch {
            try {
                val result = adminRepository.approveStory(_selectedStory.value!!.id, age, "rejected")
                result.onSuccess { message ->
                    _toast.value = message
                }.onFailure { error ->
                    Log.e("apiError", "Error: ${error.message}")
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStory.value = null
                loadStories()
            }
        }
    }

    fun deleteSelectedStory()
    {
        if(_selectedStory.value == null) return
        viewModelScope.launch {
            try{
                val result = storyDetailRepository.deleteStory(_selectedStory.value!!.id)
                result.onSuccess { data ->
                    _toast.value = data.message
                }.onFailure { error ->
                    _toast.value = "Error: ${error.message}"
                    Log.e("apiError", "Error: ${error.message}")
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
            finally {
                _selectedStory.value = null
                loadStories()
            }
        }
    }

    fun setShowDialogState(state: Boolean)
    {
        _isShowDialog.value = state
    }
}