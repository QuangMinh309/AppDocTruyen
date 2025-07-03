package com.example.frontend.ui.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.community.CommunityDetailViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.formatViews
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommunityDetailScreen(viewModel: CommunityDetailViewModel = hiltViewModel()) {
    val community by viewModel.community.collectAsState()
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
                        async { viewModel.loadCommunity() }
                    ).awaitAll()
                } catch (e: Exception) {
                    viewModel.showToast("Lỗi khi làm mới chi tiết cộng đồng: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = true,
                iconType = "Setting",
                title = "Cộng đồng",
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
                    CircularProgressIndicator(color = OrangeRed)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Community info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 35.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = community?.avatarUrl.takeIf { !it.isNullOrEmpty() } ?: R.drawable.avt_img,
                            contentDescription = "Ảnh đại diện cộng đồng",
                            placeholder = painterResource(id = R.drawable.intro_page1_bg),
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = community?.name ?: "...",
                                color = Color.White,
                                style = TextStyle(fontSize = 24.sp)
                            )
                            Text(
                                text = "${community?.memberNum?.toLong()?.let { formatViews(it) } ?: "..."} thành viên",
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp)
                            )
                            LinearButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),
                                onClick = {
                                    community?.id?.let { id ->
                                        viewModel.onGoToChattingScreen(id)
                                    }
                                }
                            ) {
                                Text(
                                    text = "Tham gia chat",
                                    color = Color.Black,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                    // Description
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionTitle(title = "Mô tả")
                        Text(
                            community?.description.takeIf { !it.isNullOrEmpty() } ?: "Không có mô tả.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    // Members list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        SectionTitle(title = "Thành viên")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.Transparent)
                        ) {
                            if (community?.members?.isEmpty() == true) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                ) {
                                    Text(
                                        text = "Không tìm thấy thành viên.",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    items(community?.members ?: emptyList()) { item ->
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.widthIn(max = 80.dp)
                                        ) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            AsyncImage(
                                                model = item.avatarUrls.takeIf { it?.isNotEmpty() == true } ?: R.drawable.avt_img,
                                                placeholder = painterResource(id = R.drawable.avt_img),
                                                contentDescription = "Ảnh đại diện",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                            Text(
                                                text = "@${item.dName}",
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                color = Color.White,
                                                style = TextStyle(fontSize = 14.sp)
                                            )
                                        }
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(170.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color.Transparent, DeepSpace)
                                        )
                                    )
                                    .align(Alignment.CenterEnd)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(80.dp)
                                        .background(color = DeepSpace)
                                        .align(Alignment.CenterEnd)
                                )
                                Button(
                                    onClick = {
                                        community?.id?.let { id ->
                                            viewModel.onGoToSearchingMemberScreen(id)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(40.dp)
                                        .align(Alignment.CenterEnd)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                shape = RoundedCornerShape(30.dp),
                                                color = DeepSpace
                                            )
                                            .border(1.dp, OrangeRed, RoundedCornerShape(30.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Xem tất cả",
                                            color = OrangeRed,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                    }
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
                contentColor = OrangeRed,
                backgroundColor = Color.White
            )
        }
    }
}