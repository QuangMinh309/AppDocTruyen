package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.model.Story
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import com.example.frontend.ui.screen.main_nav.genreDemoList
import com.example.frontend.ui.screen.story.ExamplStory
import com.example.frontend.ui.screen.story.Examplechapters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class YourStoryViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
  // Fake data
   val stories = ExampleList
    fun onGoToYourStoryDetailScreen(storyId: Int)  {
        viewModelScope.launch {
          navigationManager.navigate(Screen.YourStoryDetail.createRoute(storyId.toString()))
       }
   }
    fun CreateNewStory(){
       val newStory = ExamplStory

   }
}