package com.example.frontend.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.NotificationViewModel
import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationScreen(viewModel: NotificationViewModel = hiltViewModel()) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.fetchNotifications() }
                    ).awaitAll()
                } catch (e: Exception) {
                    viewModel.showToast("Lỗi khi làm mới thông báo: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Thông báo",
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
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(color = OrangeRed)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                ) {
                    items(notifications.size) { index ->
                        Box(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures()
                            }
                        ) {
                            NotificationCard(
                                content = notifications[index].content,
                                type = notifications[index].type,
                                time = notifications[index].createAt
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