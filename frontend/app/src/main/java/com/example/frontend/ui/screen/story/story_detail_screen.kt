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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.R
import com.example.frontend.navigation.NavigationManager
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
import com.example.frontend.ui.components.TopComments
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.launch

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
                title = "listName",
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
                    .padding(16.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                item { StoryInfo(viewModel) }
                item { Spacer(Modifier.height(19.dp)) }
                item { StoryStatusAction( isAuthor = false,storyStatus = storyStatus, hasVoted =  btnVote) }
                item { Spacer(Modifier.height(29.dp)) }
                item {
                    DescriptionStory(
                        aboutContent = {
                            Text(
                                text = "Your membership starts as soon as you set up payment and subscribe. Your monthly charge will occur on the last day of the current billing period." +
                                        "We’ll renew your membership for you can manage your subscription or turn off auto-renewal under accounts setting.",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                            Spacer(Modifier.height(29.dp))
                            LargeGenreTags(listOf("Adventure", "Mystery", "Autobiography", "Fantasy", "Drama"),
                                )
                            Spacer(Modifier.height(37.dp))
                            AuthorInfoCard (authorName = "PeneLoped Lynne", username = "tolapenelope",
                               viewModel= viewModel)
                            Spacer(Modifier.height(37.dp))
                            SectionTitle(title = "Top Comments")
                            val rawComments = listOf(
                                listOf("linh", null, R.drawable.story_detail_page1, "Chap 2", "2025-05-07", "10:10", "10", "0"),
                                listOf("huy", "Cảnh này chất!", R.drawable.intro_page3_bg, "Chap 3", "2025-05-06", "09:45", "24", "2"),
                                listOf("thu", "Truyện hay nha", null, "Chap 1", "2025-05-05", "12:30", "33", "1")
                            )
                            TopComments(rawComments.filterIsInstance<List<Any>>())
                            Spacer(Modifier.height(37.dp))
                            SectionTitle(title = "Novel Similar")
                            SimilarNovelsCard(
                                listOf(
                                    listOf(R.drawable.novel_similar, "Demon Slayer", "Koyoharu Gotouge", "25.000đ", 14952L),
                                    listOf(R.drawable.novel_similar, "One Piece", "Eiichiro Oda", "30.000đ", 8123L),
                                    listOf(R.drawable.novel_similar, "Attack on Titan", "Hajime Isayama", "20.000đ", 12045L)
                                ),
                                viewModel
                            )
                        },
                        chapterContent = {
                            val chapters = listOf(
                                listOf("Chapter1", "2025-05-01", "10:00 AM", "120", "500", true, false),
                                listOf("Chapter2", "2025-05-02", "12:30 PM", "80", "350", true, false),
                                listOf("Chapter3", "2025-05-03", "03:00 PM", "200", "1000", false, false)
                            )

                            Spacer(Modifier.height(29.dp))
                            chapters.forEachIndexed { index, chapter ->
                                ChapterItemCard(
                                    title = chapter[0] as String,
                                    date = chapter[1] as String,
                                    time = chapter[2] as String,
                                    commentCount = chapter[3] as String,
                                    viewCount = chapter[4] as String,
                                    isLocked = chapter[5] as Boolean,
                                    isAuthor = chapter[6] as Boolean,
                                    onClick = {viewModel.onGoToChapterScreen(1)}
                                )
                                if (index < chapters.lastIndex) {
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
