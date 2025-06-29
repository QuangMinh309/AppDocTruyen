package com.example.frontend.presentation.viewmodel.admin

import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.screen.main_nav.genreDemoList
import com.example.frontend.ui.screen.story.ExampleCategories
//import com.example.frontend.ui.screen.story.Examplechapters
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StoryViewMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    val categories= ExampleCategories
    val chapterrs= Examplechapters
}

val ExampleChapter: Chapter=Chapter (
    chapterId = 1,
    chapterName = "The Beginning",
    ordinalNumber = 1,
    storyId = 1,
    content = "The hero embarks on a journey to find the lost artifact...",
    viewNum = 500, commentNumber = 10,
    updatedAtString = "2025-07-15T07:30:00.000Z",
    lockedStatus = false
)

val Examplechapters: List<Chapter> = listOf(
    ExampleChapter,
    ExampleChapter,
    ExampleChapter,
    ExampleChapter,
    ExampleChapter
)

//val ExamplStory=Story(
//    id=1,
//    name ="Alibaba",
//    coverImgUrl = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//    description = "fgfssdf",
//    price = BigDecimal(10000),
//    author = User(id = 1,
//        name = "peneloped",
//        avatarUrl ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//        dName = "tolapenee"
//    ),
//    voteNum = 100,
//    chapterNum = 10,
//    viewNum = 100,
//    categories = genreDemoList,
//    createdAt = LocalDate.parse("2024-12-12"),
//    updateAt = LocalDate.parse("2024-12-12"),
//    status = "Full",
//    ageRange = 13,
//    pricePerChapter = BigDecimal(200),
//    chapters = Examplechapters
//)

val ExampleCategories = listOf(
    Category(1, "Fantasy"),
    Category(2, "Adventure")
)

//val Examplestories = listOf(
//    Story(
//        id=1,
//        name ="Alibaba",
//        coverImgUrl = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//        description = "fgfssdf",
//        price = BigDecimal(10000),
//        author = User(id = 1,
//            name = "peneloped",
//            avatarUrl ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//            dName = "tolapenee"
//        ),
//        voteNum = 100,
//        chapterNum = 10,
//        viewNum = 100,
//        categories = genreDemoList,
//        createdAt = LocalDate.parse("2024-12-12"),
//        updateAt = LocalDate.parse("2024-12-12"),
//        status = "Full",
//        ageRange = 13,
//        pricePerChapter = BigDecimal(200),
//        chapters = Examplechapters
//
//    ),
//    Story(
//        id=2,
//        name ="Alibaba",
//        coverImgUrl = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//        description = "fgfssdf",
//        price = BigDecimal(10000),
//        author = User(id = 1,
//            name = "peneloped",
//            avatarUrl ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//            dName = "tolapenee"
//        ),
//        voteNum = 100,
//        chapterNum = 10,
//        viewNum = 100,
//        categories = genreDemoList,
//        createdAt = LocalDate.parse("2024-12-12"),
//        updateAt = LocalDate.parse("2024-12-12"),
//        status = "Full",
//        ageRange = 13,
//        pricePerChapter = BigDecimal(200),
//        chapters = Examplechapters
//    )
//)
