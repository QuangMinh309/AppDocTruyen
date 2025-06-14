package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.YourStoryViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard4
import com.example.frontend.ui.components.TopBar


@Preview(showBackground = true)
@Composable
fun PreviewScreenContent4() {
    val fakeViewModel =YourStoryViewModel(NavigationManager())
    YourStoryScreen(viewModel = fakeViewModel)
}


@Preview
@Composable
fun YourStoryScreen(viewModel: YourStoryViewModel = hiltViewModel()) {
    val stories = viewModel.stories
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Your Story",
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoToNotificationScreen() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.CreateNewStory() },
                    containerColor = Color(0xFFFFAE95),
                    shape = RoundedCornerShape(50.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ), modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }
            },
            containerColor = Color.Black
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {

                Spacer(Modifier.height(20.dp))
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(8.dp)
                ){
                    items(stories) { story ->
                        StoryCard4(story = story, onClick = { viewModel.onGoToYourStoryDetailScreen(story.id) })
                    }
                }
            }
        }
    }
}
