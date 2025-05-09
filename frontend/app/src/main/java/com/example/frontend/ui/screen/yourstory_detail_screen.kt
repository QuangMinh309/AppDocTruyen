package com.example.frontend.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.*
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.launch

@Preview
@Composable
fun YourStoryDetailScreen() {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item { HeaderBar() }
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
                        .align(Alignment.BottomStart)
                        .offset(y = (-25).dp)
                        .padding(start = 16.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(43.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(OrangeRed, Color(0xFFDF4258)),
                                endX = 200f
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Start Write",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(19.dp)) }
            item { StoryStatusAction(true, storyStatus, btnVote, {}) }
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

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Thể Loại",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 21.sp,
                            fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                        )

                        Spacer(modifier = Modifier.height(19.dp))

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

                        LargeGenreTags(
                            listOf(
                                "Adventure",
                                "Mystery",
                                "Autobiography",
                                "Fantasy",
                                "Drama"
                            )
                        )
                    },
                    chapterContent = {
                        val chapters = listOf(
                            listOf("Chapter1", "2025-05-01", "10:00 AM", "120", "500", true, true),
                            listOf("Chapter2", "2025-05-02", "12:30 PM", "80", "350", true, true),
                            listOf("Chapter3", "2025-05-03", "03:00 PM", "200", "1000", false, true)
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
                                isAuthor = chapter[6] as Boolean
                            )
                            // Show divider between two chapter
                            if (index < chapters.lastIndex) {
                                Divider(
                                    color = Color.Gray,
                                    thickness = 1.2.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
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