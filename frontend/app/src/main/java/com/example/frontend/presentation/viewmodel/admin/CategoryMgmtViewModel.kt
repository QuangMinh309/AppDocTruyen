package com.example.frontend.presentation.viewmodel.admin

import android.content.Context
import android.widget.Toast
import com.example.frontend.data.model.Category
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import javax.inject.Inject

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
class CategoryMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _newName = MutableStateFlow("")
    val newName: StateFlow<String> = _newName

    private val _selectedItem = MutableStateFlow<Category?>(null)
    val selectedItem : StateFlow<Category?> = _selectedItem

    private val _categories = MutableStateFlow<List<Category>>(dummyCategories) //apiservice.getCategories()
    val categories : StateFlow<List<Category>> = _categories

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

    fun createOrUpdateCategory(context: Context)
    {
        if(_name.value.isBlank() && _newName.value.isBlank())
            Toast.makeText(context, "Please fill in the fields!", Toast.LENGTH_SHORT).show()
        else if(_newName.value.isBlank())
            Toast.makeText(context, "Please fill in the new name!", Toast.LENGTH_SHORT).show()
        else if(_name.value.isBlank())
        {
            for(item in _categories.value)
            {
                if(item.name == _newName.value)
                {
                    Toast.makeText(context, "Category already exists!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
//            _categories.value = _categories.value + Category(
//                id = _categories.value.size + 1,
//                name = _newName.value
//            )
//            _newName.value = ""
        }
        else
        {
            for(item in _categories.value)
            {
                if(item.name == _newName.value)
                {
                    Toast.makeText(context, "Category already exists!", Toast.LENGTH_SHORT).show()
                    return
                }
            }

        }
    }

    fun deleteSelectedCategory()
    {
        _categories.value = _categories.value.filter { it != _selectedItem.value }
        _selectedItem.value = null
    }
}