package com.example.frontend.ui.screen.story

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.model.Author
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Comment
import com.example.frontend.data.model.Story
import com.example.frontend.data.model.User
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.StoryDetailViewModel
import com.example.frontend.ui.components.AuthorInfoCard
import com.example.frontend.ui.components.ChapterItemCard
import com.example.frontend.ui.components.DescriptionStory
import com.example.frontend.ui.components.LargeGenreTags
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.SimilarNovelsCard
import com.example.frontend.ui.components.StoryInfo
import com.example.frontend.ui.components.StoryStatusAction
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.demoAppUser
import com.example.frontend.ui.screen.main_nav.genreDemoList
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Preview
@Composable
fun PreviewStoryDetailScreen()
{
    val fakeviewmodel=StoryDetailViewModel( NavigationManager())
    StoryDetailScreen(viewModel=fakeviewmodel)
}
@Composable
fun StoryDetailScreen(viewModel : StoryDetailViewModel = hiltViewModel()) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isFabVisible by remember {
        derivedStateOf {
            // Show FAB if scroll down to the 3rd item onwards or scroll far enough in the first item
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 300
        }
    }

 //   val StoryId=viewModel.storyId

    val storyStatus = remember { mutableStateOf("Full") }
    val btnVote = remember { mutableStateOf("Vote") }

    ScreenFrame(
        topBar = {
            TopBar(
                title = ExamplStory.name?:"",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting()}
            )
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                item { StoryInfo(viewModel) }
                item { Spacer(Modifier.height(19.dp)) }
                item { StoryStatusAction( isAuthor = true,storyStatus = storyStatus, hasVoted =  btnVote, onActionClick = {viewModel.onGoToWriteScreen(ExamplStory.id)}) }
                item { Spacer(Modifier.height(29.dp)) }
                item {
                    DescriptionStory(
                        aboutContent = {
                            Text(
                                text = ExamplStory.description.toString(),
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                            Spacer(Modifier.height(29.dp))

                            LargeGenreTags(ExamplStory.categories?: emptyList())

                            Spacer(Modifier.height(37.dp))
                        //    AuthorInfoCard (model = ExamplStory.author, onClick = {viewModel.onGoToUserProfileScreen(ExamplStory.author.id)})
                            Spacer(Modifier.height(37.dp))
                            SectionTitle(title = "Top Comments")
//                            val rawComments = listOf(
//                                listOf("linh", null, R.drawable.story_detail_page1, "Chap 2", "2025-05-07", "10:10", "10", "0"),
//                                listOf("huy", "Cảnh này chất!", R.drawable.intro_page3_bg, "Chap 3", "2025-05-06", "09:45", "24", "2"),
//                                listOf("thu", "Truyện hay nha", null, "Chap 1", "2025-05-05", "12:30", "33", "1")
//                            )
                        //    TopComments(comments, viewModel)
                            Spacer(Modifier.height(37.dp))
                            SectionTitle(title = "Novel Similar")
                            SimilarNovelsCard(Examplestories,viewModel)
                        },
                        chapterContent = {
//                            val chapters = listOf(
//                                listOf("Chapter1", "2025-05-01", "10:00 AM", "120", "500", true, false),
//                                listOf("Chapter2", "2025-05-02", "12:30 PM", "80", "350", true, false),
//                                listOf("Chapter3", "2025-05-03", "03:00 PM", "200", "1000", false, false)
//                            )
                            Spacer(Modifier.height(29.dp))
                            Examplechapters.forEachIndexed { index, chapter ->
                                ChapterItemCard(
                                    chapter = chapter,
                                    onClick = { viewModel.onGoToChapterScreen(chapter.chapterId.toString()) }
                                )

                                if (index < Examplechapters.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        thickness = 1.2.dp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }

            if (isFabVisible) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = OrangeRed,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

val ExampleChapter: Chapter=Chapter (
chapterId = 1,
chapterName = "The Beginning",
ordinalNumber = 1,
storyId = 1,
content = "The hero embarks on a journey to find the lost artifact...",
viewNum = 500, commentNumber = 10,
updateAt = LocalDate.parse("2024-12-12"),
lockedStatus = false
)

val Examplechapters: List<Chapter> = listOf(
    ExampleChapter,
    ExampleChapter,
    ExampleChapter,
    ExampleChapter,
    ExampleChapter
)

val ExamplStory=Story(
    id=1,
    name ="Alibaba",
    coverImgId = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
    description = "fgfssdf",
    price = BigDecimal(10000),
    author = Author(id = 1,
        name = "peneloped",
        avatarId ="https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",

    ),
    voteNum = 100,
    chapterNum = 10,
    viewNum = 100,
    categories = genreDemoList,
    createdAt = "2024-12-12",//LocalDate.parse("2024-12-12"),
    updateAt = "2024-12-12",//LocalDate.parse("2024-12-12"),
    status = "Full",
    ageRange = 13,
    pricePerChapter = BigDecimal(200),

)

val ExampleCategories = listOf(
    Category(1, "Fantasy"),
    Category(2, "Adventure")
)

val Examplestories = listOf(
   ExamplStory
)



val comments: List<Comment> = listOf(

    Comment(
        commentId = 2,
        user = demoAppUser, // tran_reader
        chapter= ExampleChapter,
        content = "I love the dragon fight scene!",
        commentPicId = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
        createAt = LocalDateTime.of(2025, 5, 10, 10, 0), // 2025-05-11 14:30
        likeNumber = 10,
        disLikeNumber = 0
    )



)

