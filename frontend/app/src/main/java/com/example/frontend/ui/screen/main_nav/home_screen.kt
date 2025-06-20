package com.example.frontend.ui.screen.main_nav

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.frontend.ui.components.*
import com.example.frontend.ui.screen.story.ExamplStory
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    // Lấy dữ liệu từ ViewModel
    val suggestedStories by viewModel.suggestedStories.collectAsState()
    val newStories by viewModel.newStories.collectAsState()
    val topRankingStories by viewModel.topRankingStories.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val readLists by viewModel.readLists.collectAsState()
    val isSuggestedLoading by viewModel.isSuggestedLoading.collectAsState()
    val isNewStoriesLoading by viewModel.isNewStoriesLoading.collectAsState()
    val isTopRankingLoading by viewModel.isTopRankingLoading.collectAsState()
    val isCategoriesLoading by viewModel.isCategoriesLoading.collectAsState()
    val isReadListsLoading by viewModel.isReadListsLoading.collectAsState()

    ScreenFrame {
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
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_ic),
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
                        .size(40.dp)
                        .clickable { viewModel.onGoToSetting() }
                )
            }

            Text(
                text = "Hello ${viewModel.userName}",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )

            // Banner
            AutoScrollBanner(items = bannerItems)


            // Gợi ý cho bạn
            Column(modifier = Modifier.fillMaxSize()) {
                SectionTitle(
                    title = "Gợi ý cho bạn",
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
                        CircularProgressIndicator()
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
                    title = "Truyện mới",
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
                        CircularProgressIndicator()
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
                        CircularProgressIndicator()
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
                                        .padding(start = 14.dp, top = 16.dp)
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
                    title = "Chủ đề",
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
                        CircularProgressIndicator()
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
                            Chip(text = genre.name ?: "", onClick = {})
                        }
                    }
                }
            }

            // Danh sách truyện (Read Lists)
            if (readLists.isNotEmpty() || isReadListsLoading) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(
                        title = "Danh sách truyện",
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
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                                .heightIn(max = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(readLists) { list ->
                                ReadListItem(item = list) {  }
                            }
                        }
                    }
                }
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
    BannerItem(R.drawable.banner1, "Truyen hot"),
    BannerItem(R.drawable.banner2, "Khuyen mai"),
    BannerItem(R.drawable.banner3, "Moi cap nhat")
)
val genreDemoList: List<Category> = listOf(Category(id =1,name ="Adventure"),Category(id =2,name ="Autobiography"),
    Category(id =3,name ="Mystery"),Category(id =4,name ="Romantic"))

val ExampleList: List<Story> = listOf(

    ExamplStory,
    ExamplStory,
    ExamplStory,
    ExamplStory


)

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

val readlistlist=listOf(
    ReadListItem_,
    ReadListItem_,
    ReadListItem_,
    ReadListItem_,
    ReadListItem_

)




//fun getSampleStories(category: Category, status :String,premiumStatus: String ): List<Story> {
//    return ExampleList.filter { story ->
//        val matchCategory = story.categories.any { it == category }
//        val matchStatus = if(status == "all") true else story.status == status
//        val matchPrice = when(premiumStatus){
//            "Premium" -> story.price.compareTo(BigDecimal.ZERO) != 0
//            "Free" -> story.price.compareTo(BigDecimal.ZERO) == 0
//            else -> true
//        }
//
//        matchCategory && matchStatus && matchPrice
//    }
//}

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

val demoAppUser = com.example.frontend.data.model.User(
    id = 2, name = "Peneloped Lyne",
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
    wallet = BigDecimal(0),
    dob = "2020-03-12",
)

val demoChatList  = listOf(
    Chat(
        id = 1,
        communityId = 1,
        sender = demoUser,
        content = "sdagthoruaes99ig",
        messagePicUrl = null,
        time = LocalDateTime.parse("2020-10-22T12:22:10"),
    ),
    Chat(
        id = 1,
        communityId = 1,
        sender = demoAppUser,
        content = "Hello?",
        messagePicUrl = null,
        time = LocalDateTime.parse("2020-10-22T12:22:10"),
    ),
    Chat(
        id = 1,
        communityId = 1,
        sender = demoUser,
        content = null,
        messagePicUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
        time = LocalDateTime.parse("2020-10-22T12:22:10"),
    ),
    Chat(
        id = 1,
        communityId = 1,
        sender = demoAppUser,
        content = "A",
        messagePicUrl = null,
        time = LocalDateTime.parse("2020-10-22T12:22:10"),
    ),
)
var demoCommunity = Community(
    id =1,
    name = "Food in anime",
    category = Category(1,"Fantasy"),
    avatarUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
    memberNum = 150000,
    description = "A group for everyone to talk about food seen in Anime or manga."
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
            + "A group for everyone to talk about food seen in Anime or manga"
)

