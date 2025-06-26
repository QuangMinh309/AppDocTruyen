package com.example.frontend.presentation.viewmodel.admin

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.example.frontend.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import com.example.frontend.data.model.Result

val dummyCategories: List<Category> = listOf(
    Category(
        id = 1,
        name = "Fantasy"
    ),
    Category(
        id = 2,
        name = "Adventure"
    ),
    Category(
        id = 3,
        name = "Romance"
    ),
    Category(
        id = 4,
        name = "Mystery"
    ),
    Category(
        id = 5,
        name = "Science Fiction"
    ),
    Category(
        id = 6,
        name = "Anthropology"
    ),
    Category(
        id = 7,
        name = "Action"
    ),
    Category(
        id = 8,
        name = "Medieval"
    ),
    Category(
        id = 9,
        name = "Isekai"
    ),
)

@HiltViewModel
class CategoryMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val categoryRepository : CategoryRepository,

) : BaseViewModel(navigationManager) {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _newName = MutableStateFlow("")
    val newName: StateFlow<String> = _newName

    private val _selectedItem = MutableStateFlow<Category?>(null)
    val selectedItem : StateFlow<Category?> = _selectedItem

    private val _categories = MutableStateFlow<List<Category>>(dummyCategories)
    val categories : StateFlow<List<Category>> = _categories

    private val _isCategoriesLoading = MutableStateFlow(false)
    val isCategoriesLoading : StateFlow<Boolean> = _isCategoriesLoading

    init {
        loadCategories()
    }

    private fun loadCategories()
    {
        viewModelScope.launch {
            _isCategoriesLoading.value = true
            try {
                val result = categoryRepository.getCategories()
                _categories.value = when (result)
                {
                    is Result.Success -> result.data
                    is Result.Failure -> emptyList()
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
                _categories.value = emptyList()
            }
            finally {
                _isCategoriesLoading.value = false
                _toast.value = "Loaded ${_categories.value.size} categories"
            }
        }
    }

    fun onNameChange(name: String)
    {
        _name.value = name
        for(item in _categories.value)
        {
            if(item.name == _name.value)
            {
                _selectedItem.value = item
                return
            }
            else _selectedItem.value = null
        }
    }

    fun onNewNameChange(name: String)
    {
        _newName.value = name
    }

    fun selectCategory(category: Category)
    {
        _selectedItem.value = if (_selectedItem.value == category) null else category
        _name.value = (if (_name.value == category.name) "" else category.name).toString()
    }

    fun createOrUpdateCategory()
    {
        if(_name.value.isBlank() && _newName.value.isBlank())
            _toast.value = "Please fill in the fields!"
        else if(_newName.value.isBlank())
            _toast.value = "Please fill in the new name!"
        else if(_name.value.isBlank() || _selectedItem.value == null)
        {
            for(item in _categories.value)
            {
                if(item.name == _newName.value)
                {
                    _toast.value = "Category already exists!"
                    return
                }
            }
            viewModelScope.launch {
                try{
                    val result = categoryRepository.createCategory(_newName.value)
                    when(result)
                    {
                        is Result.Success -> {
                            loadCategories()
                            _name.value = ""
                            _newName.value = ""
                        }
                        is Result.Failure -> {
                            _toast.value = "Failed to create category: ${result.exception.message}"
                        }
                    }
                }
                catch(exception: Exception) {
                    _toast.value = "Error: ${exception.message}"
                }
            }
        }
        else
        {
            for(item in _categories.value)
            {
                if(item.name == _newName.value)
                {
                    _toast.value = "Category already exists!"
                    return
                }
            }
            viewModelScope.launch {
                try{
                    val result = categoryRepository.updateCategory(selectedItem.value!!.id, _newName.value)
                    when(result)
                    {
                        is Result.Success -> {
                            _newName.value = ""
                        }
                        is Result.Failure -> {
                            _toast.value = "Failed to update category: ${result.exception.message}"
                        }
                    }
                }
                catch(exception: Exception) {
                    _toast.value = "Error: ${exception.message}"
                }
                finally {
                    loadCategories()
                }
            }
        }
    }

    fun deleteSelectedCategory()
    {
        if(_selectedItem.value == null)
        {
            _toast.value = "Please select a category!"
            return
        }
        viewModelScope.launch {
            try{
                val result = categoryRepository.deleteCategory(selectedItem.value!!.id)
                when(result)
                {
                    is Result.Success -> {
                        _selectedItem.value = null
                    }
                    is Result.Failure -> {
                        _toast.value = "Failed to delete category: ${result.exception.message}"
                    }
                }
            }
            catch(exception: Exception) {
                _toast.value = "Error: ${exception.message}"
            }
            finally {
                loadCategories()
            }
        }
    }
}