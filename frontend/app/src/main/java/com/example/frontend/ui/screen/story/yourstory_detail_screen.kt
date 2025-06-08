package com.example.frontend.ui.screen.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.YourStoryDetailViewModel
import com.example.frontend.ui.components.ChapterItemCard
import com.example.frontend.ui.components.DescriptionStory
import com.example.frontend.ui.components.LargeGenreTags
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.StoryStatusAction
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.genreDemoList
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewYourStoryDetailScreen()
{
    val fakeviewmodel= YourStoryDetailViewModel(NavigationManager())
    YourStoryDetailScreen(fakeviewmodel)
}
@Composable
fun YourStoryDetailScreen(viewModel: YourStoryDetailViewModel= hiltViewModel()) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 300
        }
    }

    val storyStatus = remember { mutableStateOf("Full") }
    val btnVote = remember { mutableStateOf("Vote") }

    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Community",
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = { /*TODO*/ },
                onRightClick = { /*TODO*/ }
            )
        }
    ){
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()

        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clickable(onClick = {
                            // TODO: handle image click
                            println("Image clicked!")
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_add_img),
                            contentDescription = "Add img",
                            colorFilter = ColorFilter.tint(OrangeRed),
                            modifier = Modifier
                                .size(47.dp)
                        )

                        Text(
                            text = "+ Thêm ảnh bìa",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.reemkufifun_wght)),
                            modifier = Modifier.padding(top = 7.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(95.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        OrangeRed.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }

                // Title of story
                Box(
                    modifier = Modifier
                        .offset(y = (-25).dp)
                        .padding(start = 16.dp)
                        .fillMaxWidth(0.6f)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    text = "+Title",
                                    color = Color.White,
                                    overflow = TextOverflow.StartEllipsis,
                                    maxLines = 1,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            item { Spacer(Modifier.height(7.dp)) }
            item {

                LinearButton(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(43.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(OrangeRed, Color(0xFFDF4258)),
                            endX = 200f
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),){
                    Text(
                        text = "Start Write",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                    )
                }

            }
            item { Spacer(Modifier.height(19.dp)) }
            item { StoryStatusAction(
                isAuthor = true,storyStatus = storyStatus, hasVoted = btnVote) }
            item { Spacer(Modifier.height(29.dp)) }
            item {
                DescriptionStory(
                    aboutContent = {
                        Spacer(Modifier.height(29.dp))
                        BasicTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 130.dp),
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.White,
                                fontSize = 16.sp
                            ),

                            decorationBox = { innerTextField ->
                                if (description.isEmpty()) {
                                    Text(
                                        text = "Thêm mô tả...",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Category")

                        Spacer(modifier = Modifier.height(16.dp))

                        BasicTextField(
                            value = category,
                            onValueChange = { category = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.DarkGray, shape = RoundedCornerShape(30.dp))
                                .padding(11.dp),
                            decorationBox = { innerTextField ->
                                if (category.isEmpty()) {
                                    Text(
                                        text = "Thêm thể loại truyện...",
                                        color = Color.LightGray,
                                        fontSize = 16.sp,
                                    )
                                }
                                innerTextField()
                            }
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        LargeGenreTags(genreDemoList)
                    },
                    chapterContent = {
                        val chapters = listOf(
                            listOf("Chapter1", "2025-05-01", "10:00 AM", "120", "500", true, true),
                            listOf("Chapter2", "2025-05-02", "12:30 PM", "80", "350", true, true),
                            listOf("Chapter3", "2025-05-03", "03:00 PM", "200", "1000", false, true)
                        )

                        Spacer(Modifier.height(29.dp))
                        Examplechapters.forEachIndexed { index, chapter ->
                            ChapterItemCard(
                                chapter = chapter,
                                onClick = {  }
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