package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Author
import com.example.frontend.data.model.Story
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.ui.screen.main_nav.ExampleList
import com.example.frontend.ui.screen.main_nav.genreDemoList
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
        val newStory = Story(
            id=1,
            name ="Alibaba",
            coverImgUrl = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            description = "",
            price = BigDecimal("10000"),
            author = Author(id = 1,
                name = "peneloped",
                avatarUrl ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
                dName = "tolapenee"
            ),
            voteNum = 100,
            chapterNum = 10,
            viewNum = 100,
            categories = genreDemoList,
            createdAt = LocalDate.parse("2024-12-12"),
            updateAt = LocalDate.parse("2024-12-12"),
            status = "Full",
            ageRange = 13,
            pricePerChapter = BigDecimal("200"),
            chapters = Examplechapters
        )

        onGoToYourStoryDetailScreen(newStory.id)
    }
}