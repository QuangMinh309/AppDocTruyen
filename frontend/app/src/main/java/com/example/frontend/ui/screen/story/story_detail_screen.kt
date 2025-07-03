package com.example.frontend.ui.screen.story

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.frontend.presentation.viewmodel.story.StoryDetailViewModel
import com.example.frontend.ui.components.AuthorInfoCard
import com.example.frontend.ui.components.ChapterItemCard
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.components.DescriptionStory
import com.example.frontend.ui.components.LargeGenreTags
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.SimilarNovelsCard
import com.example.frontend.ui.components.StoryInfo
import com.example.frontend.ui.components.StoryStatusAction
import com.example.frontend.ui.components.TopBar

import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoryDetailScreen(viewModel: StoryDetailViewModel = hiltViewModel()) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isShowDialog by viewModel.isShowDialog.collectAsState()
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 300
        }
    }

    // Đồng bộ storyStatus với viewModel.story.value?.status
    val storyStatus = remember { mutableStateOf(viewModel.story.value?.status ?: "Full") }
    LaunchedEffect(viewModel.story.value?.status) {
        viewModel.story.value?.status?.let { storyStatus.value = it }
    }

    // Quan sát hasVoted từ StateFlow
    val hasVoted by viewModel.hasVoted.collectAsState()
    val voteButtonText = remember { mutableStateOf(if (hasVoted) "Voted" else "Vote") }
    LaunchedEffect(hasVoted) {
        voteButtonText.value = if (hasVoted) "Voted" else "Vote"
    }

    val isLoading by viewModel.isLoading

    // Trạng thái cho chế độ chọn và danh sách chapter được chọn
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedChapters by remember { mutableStateOf(setOf<Int>()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // State để kiểm soát làm mới
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.loadStoryDetails() },
                        async { viewModel.loadSimilarStories() },
                        async { viewModel.loadVoteStatus() }
                    ).awaitAll()
                } catch (e: Exception) {
                 //   viewModel.setToast("Lỗi khi làm mới: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ConfirmationDialog(
        showDialog = isShowDialog,
        title="Buy chapter",
        text = "Are you sure to buy this chapter with ${viewModel.story.value?.pricePerChapter} ?",
        onConfirm = {
            viewModel.purchaseChapter()
        },
        onDismiss = {
                viewModel.setShowDialogState(false)
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                title = viewModel.story.value?.name ?: "",
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                item { StoryInfo(viewModel) }
                item { Spacer(Modifier.height(19.dp)) }
                item {
                    StoryStatusAction(
                        isAuthor = viewModel.isAuthor.value,
                        storyStatus = storyStatus,
                        hasVoted = voteButtonText,
                        onActionClick = { viewModel.onGoToWriteScreen(viewModel.storyId.value) },
                        viewModel = viewModel
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                item {
                    DescriptionStory(
                        aboutContent = {
                            Text(
                                text = viewModel.story.value?.description ?: "",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                            )
                            Spacer(Modifier.height(29.dp))
                            LargeGenreTags(viewModel.story.value?.categories ?: emptyList())
                            Spacer(Modifier.height(37.dp))
                            if (!viewModel.isAuthor.value) {
                                viewModel.story.value?.author?.let { author ->
                                    AuthorInfoCard(
                                        model = author,
                                        onClick = { viewModel.onGoToUserProfileScreen(author.id) }
                                    )
                                }
                                Spacer(Modifier.height(37.dp))
                            }
                            SectionTitle(title = "Novel Similar")
                            SimilarNovelsCard(viewModel.similarStories.value, viewModel)
                        },
                        chapterContent = {
                            Spacer(Modifier.height(29.dp))
                            if (viewModel.isAuthor.value) {
                                // Hiển thị nút xóa khi có chapter được chọn
                                if (isSelectionMode && selectedChapters.isNotEmpty()) {
                                    Button(
                                        onClick = { showDeleteDialog = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Xóa ${selectedChapters.size} chương", color = Color.White)
                                    }
                                }
                            }
                            viewModel.story.value?.chapters?.forEachIndexed { index, chapter ->
                                ChapterItemCard(
                                    chapter = chapter,
                                    isSelectionMode = isSelectionMode && viewModel.isAuthor.value,
                                    isSelected = chapter.chapterId in selectedChapters,
                                    onClick = {
                                        if(chapter.lockedStatus){
                                            viewModel.setShowDialogState(true,chapter)
                                        }
                                        else
                                             viewModel.onGoToChapterScreen(chapter.chapterId, viewModel.finalChapterId.value?:chapter.chapterId,viewModel.storyId.value,viewModel.isAuthor.value) },
                                    onLongClick = {
                                        isSelectionMode = true
                                        selectedChapters = selectedChapters + chapter.chapterId
                                    },
                                    onCheckedChange = { isChecked ->
                                        selectedChapters = if (isChecked) {
                                            selectedChapters + chapter.chapterId
                                        } else {
                                            selectedChapters - chapter.chapterId
                                        }
                                        if (selectedChapters.isEmpty()) {
                                            isSelectionMode = false
                                        }
                                    }
                                )
                                if (index < (viewModel.story.value?.chapters?.lastIndex ?: -1)) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        thickness = 1.2.dp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }

            // Hiển thị ConfirmationDialog
            ConfirmationDialog(
                showDialog = showDeleteDialog,
                title = "Xác nhận xóa",
                text = "Bạn có chắc muốn xóa ${selectedChapters.size} chương đã chọn?",
                onConfirm = {
                    scope.launch {
                        viewModel.deleteChapters(selectedChapters.toList())
                        selectedChapters = emptySet()
                        isSelectionMode = false
                        showDeleteDialog = false
                    }
                },
                onDismiss = {
                    showDeleteDialog = false
                    selectedChapters = emptySet()
                    isSelectionMode = false
                }
            )

            // Hiển thị PullRefreshIndicator
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = OrangeRed,
                backgroundColor = Color.White
            )

            if (isFabVisible) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = OrangeRed,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

