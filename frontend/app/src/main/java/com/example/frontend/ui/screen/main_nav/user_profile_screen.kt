package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.ProfileViewModel
import com.example.frontend.presentation.viewmodel.main_nav.UserProfileViewModel
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.SimilarNovelsCard
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.formatViews
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SteelBlue
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


//@Preview(showBackground = true)
//@Composable
//fun PreviewUserScreenContent2() {
//    val fakeViewModel = UserProfileViewModel(NavigationManager())
//    UserProfileScreen(viewModel = fakeViewModel)
//}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()
    val isFollowing by viewModel.isFollowing.collectAsState()
    val isLoading by viewModel.isLoading
    val isLoadingStories by viewModel.isLoadingStories
    val isLoadingFollow by viewModel.isLoadingFollow // Sử dụng isLoadingFollow
    val stories by viewModel.Stories.collectAsState()
    val storyList = viewModel.storyList
    val expanded = remember { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    listOf(
                        async { viewModel.loadUserProfile() },
                        async { viewModel.checkFollowStatus() },
                        async { viewModel.loadStories() }
                    ).awaitAll()
                } catch (e: Exception) {
                    println("Error refreshing UserProfileScreen: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = false,
                iconType = "Setting",
             //   unreadNotificationCount = unreadNotificationCount.value,
                onLeftClick = { viewModel.onGoToNotificationScreen() },
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
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Background Image
                        AsyncImage(
                            model = user.backgroundUrl,
                            contentDescription = "Profile background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            placeholder = painterResource(id = R.drawable.broken_image),
                            error = painterResource(id=R.drawable.broken_image)
                        )

                        // Profile content
                        Row(
                            modifier = Modifier
                                .height(240.dp)
                                .fillMaxWidth()
                                .padding(top = 110.dp, start = 30.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Avatar with offset and transparent border
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(
                                        width = 6.dp,
                                        color = DeepSpace,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(90.dp)
                                        .border(
                                            width = 6.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(OrangeRed, BurntCoral)
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(80.dp)
                                            .border(
                                                width = 5.dp,
                                                color = DeepSpace,
                                                shape = CircleShape
                                            )
                                    ) {
                                        AsyncImage(
                                            model = user.avatarUrl,
                                            contentDescription = "Profile avatar",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(id = R.drawable.avt_img),
                                            error = painterResource(id=R.drawable.broken_image)
                                        )
                                    }
                                }
                            }

                            // Name and nickName
                            Box(
                                modifier = Modifier
                                    .padding(start = 0.dp, top = 50.dp)
                                    .border(
                                        2.dp,
                                        Brush.linearGradient(
                                            colors = listOf(BrightAquamarine, SteelBlue)
                                        ),
                                        RoundedCornerShape(50)
                                    )
                                    .background(color = DeepSpace)
                                    .padding(5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(OrangeRed, BurntCoral)
                                            ),
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .padding(horizontal = 20.dp, vertical = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(0.dp)
                                ) {
                                    Text(
                                        text = user.name,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Text(
                                        text = "@${user.dName}",
                                        style = TextStyle(
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            // Dropdown menu
                            Box(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 20.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.three_dots),
                                    contentDescription = "Settings",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { expanded.value = true }
                                )
                                DropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Report User") },
                                        onClick = {
                                            viewModel.onGoToReportScreen(user.id, user.name)
                                            expanded.value = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Thông tin number và Follow/Unfollow button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, start = 5.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Follow/Unfollow button
                        Button(
                            onClick = { viewModel.toggleFollow() },
                            modifier = Modifier
                                .height(40.dp)
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing == true) Color.Gray else OrangeRed
                            ),
                            enabled = !isLoadingFollow // Sử dụng isLoadingFollow
                        ) {
                            if (isLoadingFollow) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = if (isFollowing == true) "Unfollow" else "Follow",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Spacer để tạo khoảng cách
                        Spacer(modifier = Modifier.width(8.dp))

                        // Các thông tin số liệu
                        StatItem(value = user.followerNum ?: 0, label = "Followers")
                        Spacer(modifier = Modifier.weight(1f))
                        StatItem(value = user.novelsNum ?: 0, label = "Novels")
                        Spacer(modifier = Modifier.weight(1f))
                        StatItem(value = user.readListNum ?: 0, label = "ReadList")
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Email and dob
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF0A2646),
                                        Color(0xFF14488E)
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(15.dp)
                    ) {
                        InforItem(Icons.Outlined.Mail, user.mail ?: "")
                        Spacer(modifier = Modifier.height(8.dp))
                        InforItem(Icons.Outlined.Cake, user.dob.toString())
                    }

                    AboutSection(content = user.about)
                    SectionTitle(title = "Author Stories")
                    GradientDivider()
                    if (isLoadingStories) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    } else {
                        SimilarNovelsCard(stories, viewModel)
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