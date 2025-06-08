package com.example.frontend.presentation.viewmodel.story

import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.story.ExampleChapter
import dagger.hilt.android.lifecycle.HiltViewModel
//import com.example.frontend.ui.screen.story.comments
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    //   savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
//val chapterID=savedStateHandle["id"]
    val chapter= ExampleChapter
//    val topcomment= comments

    fun goToNextChapter()
    {

    }
}
