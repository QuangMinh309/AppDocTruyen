package com.example.frontend.presentation.viewmodel.admin

import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Story
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.screen.main_nav.ExampleList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StoryMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private val _categories = MutableStateFlow<List<Category>>(dummyCategories) //apiservice.getCategories()
    val categories : StateFlow<List<Category>> = _categories

    private val _selectedItem = MutableStateFlow<Story?>(null)
    val selectedItem : MutableStateFlow<Story?> = _selectedItem

    private val _selectedSearchType = MutableStateFlow<String?>("Name")
    val selectedSearchType : MutableStateFlow<String?> = _selectedSearchType

    private val _selectedStates = MutableStateFlow<List<String>>(emptyList())
    val selectedStates : MutableStateFlow<List<String>> = _selectedStates

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories : MutableStateFlow<List<String>> = _selectedCategories

    private val _tbSearchValue = MutableStateFlow("")
    val tbSearchValue : MutableStateFlow<String> = _tbSearchValue

    private val _stories = MutableStateFlow<List<Story>>(ExampleList)
    val stories : MutableStateFlow<List<Story>> = _stories

    fun onSelectSearchType(type: String)
    {
        _selectedSearchType.value = type
    }

    fun onSearch(name : String)
    {
        _tbSearchValue.value = name
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
}