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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.NameListStoryViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameListStoryScreen(
    viewModel: NameListStoryViewModel = hiltViewModel()
) {
    val stories by viewModel.stories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val nameListName by viewModel.nameListName.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    // Gọi loadStories() để làm mới danh sách
                    listOf(
                        async { viewModel.loadStories() }
                    ).awaitAll()
                } catch (e: Exception) {
                    // Xử lý lỗi (có thể thay bằng Toast hoặc Snackbar sau này)
                    println("Error refreshing NameListStoryScreen: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                title = nameListName,
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
                if (isLoading && stories.isEmpty()) {
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
                        items(stories) { story ->
                            StoryCard3(
                                story = story,
                                onClick = { viewModel.onGoToStoryScreen(story.id) },
                                onDeleteClick = { viewModel.deleteStoryInNameList(story.id) },
                                showDeleteButton = true // Bật nút xóa
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
                contentColor = Color(0xFFF28C38),
                backgroundColor = Color.White
            )
        }
    }
}