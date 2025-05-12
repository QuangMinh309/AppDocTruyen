package com.example.frontend.presentation.viewmodel.story

import androidx.lifecycle.SavedStateHandle
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
        val storyId: String = checkNotNull(savedStateHandle["id"])


}