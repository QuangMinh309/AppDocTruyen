package com.example.frontend.presentation.viewmodel.main_nav

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

class StorySearchViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    //Fake data
    val stories = ExampleList

}