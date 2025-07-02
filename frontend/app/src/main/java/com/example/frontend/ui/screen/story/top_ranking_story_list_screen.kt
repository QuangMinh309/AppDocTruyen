package com.example.frontend.ui.screen.story

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.StoryListViewModel
import com.example.frontend.presentation.viewmodel.story.TopRankingStoryListViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.ExampleList


@Composable
fun TopRankingStoryListScreen(viewModel:TopRankingStoryListViewModel= hiltViewModel()) {
    val listName="Top Ranking"

    val topRankingStories by viewModel.topRankingStories.collectAsState()
    val isTopRankingLoading by viewModel.isTopRankingLoading.collectAsState()


    ScreenFrame(
        topBar = {
            TopBar(
                title = listName,
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack()},
                onRightClick = {viewModel.onGoToSetting() }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            if (isTopRankingLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else {
                // (LazyColumn)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(topRankingStories) { story ->
                        StoryCard3(story = story, onClick = {
                          viewModel.onGoToStoryScreen(story.id)
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
