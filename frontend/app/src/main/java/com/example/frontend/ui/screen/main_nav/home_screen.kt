package com.example.frontend.ui.screen.main_nav

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.data.model.Author
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chat
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.ReadList
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.HomeViewModel
import com.example.frontend.ui.components.AutoScrollBanner
import com.example.frontend.ui.components.BannerItem
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.StoryCard
import com.example.frontend.ui.components.StoryCard2
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.story.Examplechapters
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val fakeViewModel = HomeViewModel(NavigationManager())
    HomeScreen(viewModel = fakeViewModel)
}

@Composable
fun HomeScreen(viewModel : HomeViewModel = hiltViewModel()) {
//    val suggestedStories = remember { mutableStateListOf<StoryItemModel>() }
//    val newStories = remember { mutableStateListOf<StoryItemModel>() }
//    val topRankingStories= remember { mutableStateListOf<StoryItemModel>() }

    val isStoriesLoading by remember { mutableStateOf(false) }
    val storyList =  ExampleList


//    LaunchedEffect(Unit) {
//        viewModel.loadUpcoming().observeForever {
//            upcoming.clear()
//            upcoming.addAll(it)
//            showUpcomingLoad = false
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadItems().observeForever {
//            newMovies.clear()
//            newMovies.addAll(it)
//            showNewMoviesLoading = false
//        }
//    }

    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = {viewModel.onGoToNotificationScreen()},
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Hello penelope !",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            //Banner
            AutoScrollBanner(items = bannerItems)

            // Gợi ý cho bạn
            Column(
                modifier = Modifier.fillMaxSize()
            )
            {
                SectionTitle(title = "Gợi ý cho bạn ")
                if (isStoriesLoading)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(storyList) { item ->
                            StoryCard(item, onClick = {viewModel.onGoToStoryScreen(item.id)})
                        }
                    }
                }

            }

            // Truyên mới
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                SectionTitle(title = "Truyện mới")
                if (isStoriesLoading)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    // Chia list thành các cặp 2 story (nếu lẻ thì item cuối chỉ có 1 story)
                    val pairedStories = storyList.chunked(2)

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(pairedStories) { pair ->
                            Column(
                                modifier = Modifier.wrapContentSize(), // Chiều rộng cố định cho mỗi cột
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Card trên
                                StoryCard2(
                                    story = pair[0],
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = { viewModel.onGoToStoryScreen(pair[0].id) }
                                )

                                // Card dưới (nếu có)
                                if (pair.size > 1) {
                                    StoryCard2(
                                        story = pair[1],
                                        modifier = Modifier.wrapContentSize(),
                                        onClick = { viewModel.onGoToStoryScreen(pair[1].id) }
                                    )
                                } else {
                                    // Nếu không có story thứ 2, để khoảng trống
                                    Spacer(modifier = Modifier.height(132.dp)) // = height card + spacing
                                }
                            }
                        }
                    }
                }
            }

            // top ranking
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // Đẩy các thành phần ra hai đầu
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    SectionTitle(title = "Top Ranking",modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {},
                    ) {
                        Text(
                            text = "Show full list >",
                            color = Color.White,
                            fontSize = 8.sp
                        )
                    }
                }
                if (isStoriesLoading)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    val top5Stories = storyList.take(5) // Chỉ lấy 5 item đầu

                    LazyColumn(
                        modifier = Modifier.height(1000.dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(top5Stories.size) { index ->
                            val story = top5Stories[index]

                            Box(modifier = Modifier.fillMaxWidth()) {
                                StoryCard3(
                                    story = story,
                                    modifier = Modifier.fillMaxWidth().padding(start = 14.dp,top = 16.dp)
                                )

                                // Thêm huy chương cho top 3
                                if (index < 3) {
                                    val iconColor = when (index) {
                                        0 ->  Color(0xFFFFD700)// Vàng
                                        1 ->Color(0xFF969191)// Bạc
                                        else -> Color(0xFFCE7320) // Đồng
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.crown_icon),
                                        contentDescription = "Rank ${index + 1}",
                                        tint = iconColor,

                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()){
                SectionTitle(title = "Danh sách truyện liên quan")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(storyList) { item ->
                        ReadListItem(item = ReadListItem_, onClick = {viewModel.onGoToStoryScreen(item.id)})
                    }
                }

            }

        }
    }
}







val bannerItems = listOf(
    BannerItem(R.drawable.banner1, "Truyen hot"),
    BannerItem(R.drawable.banner2, "Khuyen mai"),
    BannerItem(R.drawable.banner3, "Moi cap nhat")
)
val genreDemoList: List<Category> = listOf(Category(id =1,name ="Adventure"),Category(id =2,name ="Autobiography"),
    Category(id =3,name ="Mystery"),Category(id =4,name ="Romantic"))

val ExampleList: List<Story> = listOf(
    Story(
        id=1,
        name ="Alibaba",
        coverImgUrl = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
        description = "fgfssdf",
        price = BigDecimal(10000),
        author = Author(id = 1,
            name = "peneloped",
            avatarUrl ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            dName = "tolapenee"
        ),
        voteNum = 100,
        chapterNum = 10,
        viewNum = 100,
        categories = genreDemoList,
                createdAt = LocalDate.parse("2024-12-12"),
        updateAt = LocalDate.parse("2024-12-12"),
        status = "Full",
        ageRange = 13,
        pricePerChapter = BigDecimal(200),
        chapters = Examplechapters
    )

)


var  ReadListItem_= ReadList(
    id=1,
    name="item sdsdfcsvsv svdvs ",
    description = "hahadfgdfgsdfsdfsdfsdfsdfdsfdsgdsfgdfgfdgfdgdf",
    userId = 2,
    stories = ExampleList
)





fun getSampleStories(category: Category, status :String,premiumStatus: String ): List<Story> {
    return ExampleList.filter { story ->
        val matchCategory = story.categories.any { it == category }
        val matchStatus = if(status == "all") true else story.status == status
        val matchPrice = when(premiumStatus){
            "Premium" -> story.price.compareTo(BigDecimal.ZERO) != 0
            "Free" -> story.price.compareTo(BigDecimal.ZERO) == 0
            else -> true
        }

        matchCategory && matchStatus && matchPrice
    }
}

val demoUser =  User(
    id = 1,
    name = "Peneloped Lyne",
    role = Role(1,"User"),
    dName = "tolapenelopee",
    backgroundUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
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
    dob = LocalDate.parse("2020-03-12"),
    isPremium = true

)

val demoAppUser = User(id = 2, name = "Peneloped Lyne",
            role = Role(1,"User"),
            dName = "tolapenelopee",
            backgroundUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
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
            dob = LocalDate.parse("2020-03-12")
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
