package com.example.frontend.presentation.viewmodel.main_nav

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor( navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
//    private val repository=MainRepository()
//
//    fun loadUpcoming(): LiveData<MutableList<FilmItemModel>> {
//        return repository.loadUpcoming()
//    }
//
//    fun loadItems(): LiveData<MutableList<FilmItemModel>> {
//        return repository.loadItems()
//    }

}