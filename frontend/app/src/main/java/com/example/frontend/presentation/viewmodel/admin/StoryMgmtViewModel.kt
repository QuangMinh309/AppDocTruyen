package com.example.frontend.presentation.viewmodel.admin

import com.example.frontend.data.model.Story
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.screen.main_nav.ExampleList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StoryMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {

    private val _selectedItem = MutableStateFlow<Story?>(null)
    val selectedItem : MutableStateFlow<Story?> = _selectedItem

    private val _stories = MutableStateFlow<List<Story>>(ExampleList)
    val stories : MutableStateFlow<List<Story>> = _stories

    fun openSettings()
    {

    }
}