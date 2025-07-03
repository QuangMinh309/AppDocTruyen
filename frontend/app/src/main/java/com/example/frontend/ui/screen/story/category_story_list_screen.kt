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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.story.CategoryStoryListViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryStoryListScreen(
    viewModel: CategoryStoryListViewModel = hiltViewModel()
) {
    val categoryName by viewModel.categoryName.collectAsState()
    val categoryStories by viewModel.categoryStories.collectAsState()
    val isLoading by viewModel.isCategoryStoriesLoading.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.loadStories() }
                    ).awaitAll()
                } catch (e: Exception) {
                    viewModel.showToast("Lỗi khi làm mới danh sách truyện: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
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
                        CircularProgressIndicator(color = OrangeRed)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(categoryStories) { story ->
                            StoryCard3(
                                story = story,
                                onClick = { viewModel.onGoToStoryScreen(story.id) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = OrangeRed,
                backgroundColor = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}