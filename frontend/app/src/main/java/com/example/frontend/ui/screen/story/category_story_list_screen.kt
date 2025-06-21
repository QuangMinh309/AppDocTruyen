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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.CategoryStoryListViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar

//@Composable
//@Preview
//fun PreCategoryStoryListScreen() {
//    val fakeViewModel = CategoryStoryListViewModel(NavigationManager())
//    CategoryStoryListScreen(fakeViewModel)
//}

@Composable
fun CategoryStoryListScreen(
    viewModel: CategoryStoryListViewModel = hiltViewModel(),
    categoryId: Int,
    categoryName: String
) {
    // Truyền categoryId và categoryName vào ViewModel
    LaunchedEffect(categoryId, categoryName) {
        viewModel.setCategoryId(categoryId)
    }

    val categoryStories by viewModel.categoryStories.collectAsState()
    val isLoading by viewModel.isCategoryStoriesLoading.collectAsState()

    ScreenFrame(
        topBar = {
            TopBar(
                title = categoryName,
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(categoryStories) { story ->
                        StoryCard3(story = story, onClick = { viewModel.onGoToStoryScreen(story.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}