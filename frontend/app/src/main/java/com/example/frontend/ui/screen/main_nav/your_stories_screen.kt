package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.frontend.presentation.viewmodel.main_nav.YourStoryViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard4
import com.example.frontend.ui.components.TopBar
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun YourStoryScreen(viewModel: YourStoryViewModel = hiltViewModel()) {
    val stories by viewModel.stories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

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
                    println("Error refreshing YourStoryScreen: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

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
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onGoToCreateStoryScreen()
                    },
                    containerColor = Color(0xFFFFAE95),
                    shape = RoundedCornerShape(50.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ),
                    modifier = Modifier.size(48.dp)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .pullRefresh(pullRefreshState)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    if (isLoading && stories.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(stories) { story ->
                                StoryCard4(
                                    story = story,
                                    onClick = { viewModel.onGoToUpdateStoryScreen(story.id) },
                                    onDeleteClick = { viewModel.deleteStory(story.id) },
                                    showDeleteButton = true
                                )
                            }
                            if (isLoading && stories.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
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
}
//// Extension function để kiểm tra cuộn đến cuối
//fun LazyListState.isScrolledToEnd(): Boolean {
//    val layoutInfo = layoutInfo
//    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
//    return if (lastVisibleItem != null) {
//        val lastItemOffset = lastVisibleItem.offset + lastVisibleItem.size
//        val viewportHeight = layoutInfo.viewportSize.height
//        lastVisibleItem.index == layoutInfo.totalItemsCount - 1 && lastItemOffset <= viewportHeight
//    } else {
//        false
//    }
//}