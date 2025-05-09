package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import com.example.frontend.domain.ReadListItemModel
import com.example.frontend.domain.StoryItemModel

@Preview
@Composable
fun HomeScreen() {

    val viewModel = HomeViewModel()

    val suggestedStories = remember { mutableStateListOf<StoryItemModel>() }
    val newStories = remember { mutableStateListOf<StoryItemModel>() }
    val topRankingStories= remember { mutableStateListOf<StoryItemModel>() }

    var showSuggestedStoriesLoading by remember { mutableStateOf(false) }
    var showNewStoriesLoading by remember { mutableStateOf(false) }
    var showTopRankingStoriesLoading by remember { mutableStateOf(false) }


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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF2F1B24))

    ) {

        Spacer(modifier = Modifier.height(30.dp))

        //TitleBar
        TitleBar()

        //Banner
        AutoScrollBanner(items = bannerItems)

        // Gợi ý cho bạn
        //  SectionTitle("Gợi ý cho bạn")

        HorizontalStoryList(isStoriesLoading = showNewStoriesLoading, storyList =  ExampleList)

        // Truyên mới
        //    SectionTitle("Truyện mới")
        TwoLineHorizontalStoryList(isStoriesLoading = showSuggestedStoriesLoading, stories = ExampleList )

        TopStoriesRanking(isStoriesLoading = showTopRankingStoriesLoading, stories = ExampleList)
        ReadListItem(ReadListItem_)

    }
}







val bannerItems = listOf(
    BannerItem(R.drawable.banner1, "Truyện hot"),
    BannerItem(R.drawable.banner2, "Khuyến mãi"),
    BannerItem(R.drawable.banner3, "Mới cập nhật")
)


var ExampleList: List<StoryItemModel> = listOf(
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120

    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://baodongnai.com.vn/file/e7837c02876411cd0187645a2551379f/022024/18_1_20240229171501.jpg",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSb3Fs752Mx1GL4D6g6EpDw4uPms47KDqDdvQ&s",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI3OOV4uJq_HKA2ReFpTaTT7y0eo7bQ3Hr7Q&s",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    ),
    StoryItemModel(
        id="1",
        title = "Alibaba",
        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
        shortDescription = "",
        isPremium = true,
        author = "Hhahaa",
        likes = 100,
        chaptersnumbers = 10,
        genres = listOf("Adventure","Mystery","Autobiography"),
        lastUpdated = "22/12/2024",
        views = 120
    )

)

var  ReadListItem_= ReadListItemModel(
    id="1",
    name="Yêu thích",
    date="4/5/2025",
    description = "hahadfgdfgsdfsdfsdfsdfsdfdsfdsgdsfgdfgfdgfdgdf",
    stories = listOf(
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120

        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://baodongnai.com.vn/file/e7837c02876411cd0187645a2551379f/022024/18_1_20240229171501.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSb3Fs752Mx1GL4D6g6EpDw4uPms47KDqDdvQ&s",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI3OOV4uJq_HKA2ReFpTaTT7y0eo7bQ3Hr7Q&s",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        )


    )
)










