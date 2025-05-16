package com.example.frontend.presentation.viewmodel.story

import androidx.lifecycle.SavedStateHandle
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class WriteScreenViewModel  @Inject constructor(
 //   savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
//val chapterID=savedStateHandle["id"]
}