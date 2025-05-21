package com.example.frontend.presentation.viewmodel.main_nav

import com.example.frontend.data.model.Category
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StorySearchViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    //Fake data
    val stories = ExampleList
    val categories  = listOf(
        Category(id =1,name ="Adventure"),
        Category(id =2,name ="Autobiography"),
        Category(id =3,name ="Mystery")
    )
    val statuses = listOf("Full", "Updated", "Premium", "Free")

    private var _searchQuery  = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private var _selectedGenreTabIndex  = MutableStateFlow<Int>(0)
    val selectedGenreTabIndex : StateFlow<Int> =  _selectedGenreTabIndex

    fun onSelectedGenreTabIndexChange(index: Int) {
        _selectedGenreTabIndex.value = index
    }
    private var _selectedStatusTabIndex  = MutableStateFlow<Int>(0)
    val selectedStatusTabIndex : StateFlow<Int> = _selectedStatusTabIndex

    fun onSelectedStatusTabIndexChange(index: Int) {
        _selectedStatusTabIndex.value = index
    }

}