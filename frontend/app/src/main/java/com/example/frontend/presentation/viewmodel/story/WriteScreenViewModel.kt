package com.example.frontend.presentation.viewmodel.story

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteScreenViewModel  @Inject constructor(
 //   savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
//val chapterID=savedStateHandle["id"]
}