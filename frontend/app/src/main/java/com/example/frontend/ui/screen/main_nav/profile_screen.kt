package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.frontend.data.model.NameList
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.ProfileViewModel
import com.example.frontend.ui.components.ConfirmationDialog
import com.example.frontend.ui.components.InputDialog
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()
    val readLists by viewModel.readLists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val unreadNotificationCount = viewModel.unReadNotificationsCount.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    // Gọi đồng thời loadUserData và loadReadLists
                    listOf(
                        async { viewModel.loadUserData() },
                        async { viewModel.loadReadLists() }
                    ).awaitAll()
                } catch (e: Exception) {
                    // Xử lý lỗi (có thể thay bằng Toast hoặc Snackbar sau này)
                    println("Error refreshing ProfileScreen: ${e.message}")
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    // State cho InputDialog và ConfirmationDialog
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedReadList by remember { mutableStateOf<NameList?>(null) }

    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = false,
                iconType = "Setting",
                unreadNotificationCount = unreadNotificationCount.value,
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
                if (isLoading || user == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    user?.let { currentUser ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Background Image
                            AsyncImage(
                                model = currentUser.backgroundUrl,
                                contentDescription = "Profile background",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                placeholder = painterResource(id = R.drawable.broken_image),
                                error = painterResource(R.drawable.broken_image)
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
                                                model = currentUser.avatarUrl,
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
                                        .padding(start = 16.dp, top = 50.dp)
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
                                            text = currentUser.name,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                fontSize = 14.sp
                                            )
                                        )
                                        Text(
                                            text = "@${currentUser.dName}",
                                            style = TextStyle(
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Thông tin số lượng
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem(value = currentUser.followerNum ?: 0, label = "Followers")
                            StatItem(value = currentUser.novelsNum ?: 0, label = "Novels")
                            StatItem(value = currentUser.readListNum ?: 0, label = "ReadList")
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
                            InforItem(Icons.Outlined.Mail, currentUser.mail ?: "")
                            Spacer(modifier = Modifier.height(8.dp))
                            InforItem(Icons.Outlined.Cake, currentUser.dob ?: "")
                        }

                        AboutSection(content = currentUser.about)

                        // Danh sách truyện (Read Lists)
                        if (readLists.isNotEmpty() || isLoading) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                SectionTitle(title = "StoryList")
                                GradientDivider()
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
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 200.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(readLists) { list ->
                                            ReadListItem(
                                                item = list,
                                                onClick = { viewModel.onGoToNameListStoryScreen(list.id) },
                                                showMoreOptions = true,
                                                onUpdateClick = { selectedReadList = it; showUpdateDialog = true },
                                                onDeleteClick = { selectedReadList = it; showDeleteDialog = true }
                                            )
                                        }
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
                contentColor = Color(0xFFF28C38),
                backgroundColor = Color.White
            )
        }

        // InputDialog cho Update Read List
        if (showUpdateDialog && selectedReadList != null) {
            InputDialog(
                showDialog = showUpdateDialog,
                title = "Update Read List",
                initialReadListName = selectedReadList!!.name,
                initialDescription = selectedReadList!!.description,
                onConfirm = { name, description ->
                    viewModel.updateReadList(selectedReadList!!.id, name, description)
                    showUpdateDialog = false
                    selectedReadList = null
                },
                onDismiss = {
                    showUpdateDialog = false
                    selectedReadList = null
                }
            )
        }

        // ConfirmationDialog cho Delete Read List
        if (showDeleteDialog && selectedReadList != null) {
            ConfirmationDialog(
                showDialog = showDeleteDialog,
                title = "Delete Read List",
                text = "Are you sure you want to delete ${selectedReadList!!.name}?",
                onConfirm = {
                    viewModel.deleteReadList(selectedReadList!!.id)
                    showDeleteDialog = false
                    selectedReadList = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    selectedReadList = null
                }
            )
        }
    }
}

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    title: String = "About",
    content: String?
) {
    Column(modifier = modifier) {
        // Tiêu đề "About"
        SectionTitle(title=title)
        // Đường phân cách
        GradientDivider()

        // Nội dung
        Text(
            text = buildAnnotatedString {
                append(content?.substringBefore("\n") ?:"No description available...")
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = Color.Gray
        )
    }
}
@Composable
fun GradientDivider() {
    Box(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .width(100.dp)
            .height(2.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF76800), // Màu xanh lá
                        Color(0xFF294C74)  // Màu xanh dương
                    )
                )
            )
    )
}
@Composable
fun InforItem(icon : ImageVector, value: String)
{

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint= OrangeRed
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text=value,
            color= Color.White,
            fontSize = 14.sp
        )
    }

}
@Composable
fun StatItem(value: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Text(
            text = formatViews(value.toLong()),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
    }
}
