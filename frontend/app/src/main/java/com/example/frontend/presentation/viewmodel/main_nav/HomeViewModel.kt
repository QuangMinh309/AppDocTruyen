package com.example.frontend.presentation.viewmodel.main_nav

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor( navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private var suggestedStories = ExampleList //listOf(MutableStateFlow<Story>(ExampleList.get(0)))
    private var newStories =ExampleList
    private var topRankingStories= ExampleList
    private var relatedStoryList= ExampleList

    fun getSuggestedStories() = suggestedStories
    fun getNewStories() = newStories
    fun getTopRankingStories() = topRankingStories
    fun getRelatedStoryList() = topRankingStories


}