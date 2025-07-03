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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.main_nav.CommunityViewModel
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.GerneChipButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommunityScreen(viewModel: CommunityViewModel = hiltViewModel()) {
    val hotCommunityList by viewModel.hotCommunities.collectAsState()
    val communitiesFollowCategory by viewModel.communitiesFollowCategory.collectAsState()
    val category by viewModel.category.collectAsState()
    val isLoadingHotCommunities by viewModel.isLoadingHotCommunities.collectAsState()
    val isLoadingCommunitiesFollowCategory by viewModel.isLoadingCommunitiesFollowCategory.collectAsState()
    val unreadNotificationCount by viewModel.unReadNotificationsCount.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.loadCategories() },
                        async { viewModel.fetchHotCommunities() },
                        async {
                            if (category.isNotEmpty()) {
                                viewModel.fetchCommunitiesFollowCategory(category[0].id)
                            }
                        }
                    ).awaitAll()
                } catch (e: Exception) {
                    viewModel.showToast("Lỗi khi làm mới dữ liệu: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Community",
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoToNotificationScreen() },
                unreadNotificationCount = unreadNotificationCount,
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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                LazyRow(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(category) { item ->
                        GerneChipButton(
                            genre = item,
                            onClick = { viewModel.fetchCommunitiesFollowCategory(item.id) }
                        )
                    }
                }

                SectionTitle(title = "Find Communities", modifier = Modifier.padding(start = 20.dp))

                // Community follow category
                if (isLoadingCommunitiesFollowCategory) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeRed)
                    }
                } else if (communitiesFollowCategory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp)
                    ) {
                        Text(
                            text = "Không tìm thấy cộng đồng nào.",
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier
                    ) {
                        items(communitiesFollowCategory) { item ->
                            CommunityCard(
                                model = item,
                                onClick = { viewModel.onGoToCommunityDetailScreen(item.id) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle(title = "Hot Communities", modifier = Modifier.padding(start = 20.dp))

                if (isLoadingHotCommunities) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeRed)
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier
                    ) {
                        items(hotCommunityList) { item ->
                            CommunityCard(
                                model = item,
                                onClick = { viewModel.onGoToCommunityDetailScreen(item.id) }
                            )
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