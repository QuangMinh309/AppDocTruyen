package com.example.frontend.ui.screen.main_nav

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chat
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.NameList
import com.example.frontend.data.model.NameListStory
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.Story
import com.example.frontend.presentation.viewmodel.main_nav.HomeViewModel
import com.example.frontend.ui.components.AutoScrollBanner
import com.example.frontend.ui.components.BannerItem
import com.example.frontend.ui.components.Chip
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.StoryCard
import com.example.frontend.ui.components.StoryCard2
import com.example.frontend.ui.components.StoryCard3

import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    // Lấy dữ liệu từ ViewModel
    RequestNotificationPermission()
    val suggestedStories by viewModel.suggestedStories.collectAsState()
    val newStories by viewModel.newStories.collectAsState()
    val topRankingStories by viewModel.topRankingStories.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val readLists by viewModel.readLists.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isSuggestedLoading by viewModel.isSuggestedLoading.collectAsState()
    val isNewStoriesLoading by viewModel.isNewStoriesLoading.collectAsState()
    val isTopRankingLoading by viewModel.isTopRankingLoading.collectAsState()
    val isCategoriesLoading by viewModel.isCategoriesLoading.collectAsState()
    val isReadListsLoading by viewModel.isReadListsLoading.collectAsState()
    val isUserLoading by viewModel.isUserLoading.collectAsState()
    val unreadNotificationCount = viewModel.unReadNotificationsCount.collectAsState()

    // State để kiểm soát làm mới
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                try {
                    // Gọi các hàm tải dữ liệu từ ViewModel song song
                    listOf(
                        async { viewModel.loadUser() },
                        async { viewModel.loadStories() },
                        async { viewModel.loadCategories() },
                        async { viewModel.loadReadLists() }
                    ).awaitAll()
                } catch (e: Exception) {
                    // Xử lý lỗi
                    Log.e("HomeScreen", "Error refreshing data: ${e.message}")
                    // TODO: Có thể thêm Toast hoặc Snackbar để thông báo lỗi
                } finally {
                    isRefreshing = false
                }
            }
        }
    )

    // Reload dữ liệu khi cần
    LaunchedEffect(Unit) {
        viewModel.loadUser()
        viewModel.loadStories()
        viewModel.loadCategories()
        viewModel.loadReadLists()
    }

    ScreenFrame {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(if (unreadNotificationCount.value == 0) R.drawable.notification_ic else R.drawable.notification) ,
                        contentDescription = "Notification",
                        tint = Color.White,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { viewModel.onGoToNotificationScreen() }
                    )

                    Text(
                        text = "Home",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.setting_ic),
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { viewModel.onGoToSetting() }
                    )
                }

                // Chào người dùng
                if (isUserLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeRed)
                    }
                } else {
                    Text(
                        text = "Hello ${currentUser?.dName ?: "User"}",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                // Banner
                AutoScrollBanner(items = bannerItems)

                // Gợi ý cho bạn
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionTitle(
                        title = "Suggested Stories",
                        modifier = Modifier.padding(start = 20.dp),
                        iconResId = R.drawable.dolphins_ic
                    )
                    if (isSuggestedLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = OrangeRed)
                        }
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(suggestedStories) { item ->
                                Log.d("HomeScreen", "Rendering StoryCard: ${item.name}")
                                StoryCard(item, onClick = { viewModel.onGoToStoryScreen(item.id) })
                            }
                        }
                    }
                }

                // Truyện mới
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionTitle(
                        title = "New Arrivals",
                        modifier = Modifier.padding(start = 20.dp),
                        iconResId = R.drawable.firework_ic
                    )
                    if (isNewStoriesLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = OrangeRed)
                        }
                    } else {
                        val pairedStories = newStories.chunked(2)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(pairedStories) { pair ->
                                Column(
                                    modifier = Modifier.wrapContentSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    StoryCard2(
                                        story = pair[0],
                                        modifier = Modifier.wrapContentSize(),
                                        onClick = { viewModel.onGoToStoryScreen(pair[0].id) }
                                    )
                                    if (pair.size > 1) {
                                        StoryCard2(
                                            story = pair[1],
                                            modifier = Modifier.wrapContentSize(),
                                            onClick = { viewModel.onGoToStoryScreen(pair[1].id) }
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(132.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Top Ranking
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionTitle(
                        title = "Top Ranking",
                        modifier = Modifier.padding(start = 20.dp),
                        iconResId = R.drawable.fire_ic
                    )
                    if (isTopRankingLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = OrangeRed)
                        }
                    } else {
                        val top5Stories = topRankingStories.take(5)
                        LazyColumn(
                            modifier = Modifier
                                .height(1000.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(top5Stories.size) { index ->
                                val story = top5Stories[index]
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    StoryCard3(
                                        story = story,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 14.dp, top = 16.dp),
                                        onClick = { viewModel.onGoToStoryScreen(story.id) }
                                    )
                                    if (index < 3) {
                                        val iconColor = when (index) {
                                            0 -> Color(0xFFFFD700) // Vàng
                                            1 -> Color(0xFF969191) // Bạc
                                            else -> Color(0xFFCE7320) // Đồng
                                        }
                                        Icon(
                                            painter = painterResource(R.drawable.crown_icon),
                                            contentDescription = "Rank ${index + 1}",
                                            tint = iconColor,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "Show Top Ranking list >>",
                        color = Color.Black,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(OrangeRed, BurntCoral)
                                ),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(horizontal = 10.dp)
                            .clickable { viewModel.onGoToTopRankingStoryListScreen() }
                    )
                }

                // Chủ đề (Categories)
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionTitle(
                        title = "Categories",
                        modifier = Modifier.padding(start = 20.dp),
                        iconResId = R.drawable.flower_ic
                    )
                    if (isCategoriesLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = OrangeRed)
                        }
                    } else {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { genre ->
                                Chip(text = genre.name ?: "", onClick = {
                                    Log.d("HomeScreen", "Chip clicked: categoryId=${genre.id}, categoryName=${genre.name}")
                                    viewModel.onGoToCategoryStoryList(genre.id, genre.name.toString())
                                })
                            }
                        }
                    }
                }

                // Danh sách truyện (Read Lists)
                if (readLists.isNotEmpty() || isReadListsLoading) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionTitle(
                            title = "Read Lists",
                            modifier = Modifier.padding(start = 20.dp),
                            iconResId = R.drawable.book_ic
                        )
                        if (isReadListsLoading) {
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(readLists) { list ->
                                    ReadListItem(
                                        item = list,
                                        onClick = { viewModel.onGoToNameListStoryScreen(list.id) }
                                    )
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
@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("Permission", "Thông báo đã được cấp quyền")
        } else {
            Log.d("Permission", "Người dùng từ chối quyền thông báo")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

val categories: List<Category> = listOf(
    Category(
        id = 1,
        name = "Fantasy"
    ),
    Category(
        id = 2,
        name = "Adventure"
    ),
    Category(
        id = 3,
        name = "Romance"
    ),
    Category(
        id = 4,
        name = "Mystery"
    ),
    Category(
        id = 5,
        name = "Science Fiction"
    )
)




val bannerItems = listOf(
    BannerItem(R.drawable.banner_005, "Truyen hot"),
    BannerItem(R.drawable.banner_006, "Khuyen mai"),
    BannerItem(R.drawable.banner007, "Moi cap nhat"),
    BannerItem(R.drawable.banner_008, "Moi cap nhat"),
    BannerItem(R.drawable.banner09, "Moi cap nhat"),
    BannerItem(R.drawable.banner10, "Moi cap nhat"),
    BannerItem(R.drawable.banner11, "Moi cap nhat"),
    BannerItem(R.drawable.banner12, "Moi cap nhat")

)
val genreDemoList: List<Category> = listOf(Category(id =1,name ="Adventure"),Category(id =2,name ="Autobiography"),
    Category(id =3,name ="Mystery"),Category(id =4,name ="Romantic"))


var ExampleNameListStory= NameListStory(
    coverImgId = "02"
)

var  ReadListItem_= NameList(
    id =1,
    name ="Yêu thích ",
    description = "Như tiêu đề thì em mới tập tành xem phim điện ảnh. Em không thích phim chính kịch cho lắm, mọi người giới thiệu cho em vài phim điện ảnh Âu Mĩ, Trung Quốc mà mọi người ấn tượng với ạ. Em cảm ơn.",
    userId = 2,
    stories = listOf(
        ExampleNameListStory,
        ExampleNameListStory
    ),
    storyCount = 2
)




val demoUser = com.example.frontend.data.model.User(
    id = 1,
    name = "Peneloped Lyne",
    role = Role(1, "User"),
    dName = "tolapenelopee",
    backgroundId = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
    mail = "peneloped@gmail.com",
    followerNum = 200,
    novelsNum = 50,
    readListNum = 3,
    about = "Your membership starts as soon as you set up payment and subscribe. " +
            "Your monthly charge will occur on the last day of the current billing period. " +
            "We'll renew your membership for you can manage your subscription or turn off " +
            "auto-renewal under accounts setting.\n" +
            "By continuing, you are agreeing to these terms. See the private statement and restrictions.",
    wallet = BigDecimal(500.00),
    dob = "2020-03-12",
    isPremium = true

)


