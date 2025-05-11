package com.example.frontend.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val navigationManager: NavigationManager) : ViewModel() {
//    private val repository=MainRepository()
//
//    fun loadUpcoming(): LiveData<MutableList<FilmItemModel>> {
//        return repository.loadUpcoming()
//    }
//
//    fun loadItems(): LiveData<MutableList<FilmItemModel>> {
//        return repository.loadItems()
//    }

    fun onGoToSearchScreen() {
        viewModelScope.launch {
            navigationManager.navigate("search")
        }
    }
}