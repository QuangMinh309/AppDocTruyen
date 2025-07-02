package com.example.frontend.ui.screen.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.ReadViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.TopComments


@Composable
fun ReadScreen(viewModel: ReadViewModel = hiltViewModel()) {
    var comment by remember { mutableStateOf("") }
    val currentChapter by viewModel.currentChapter.collectAsState()
    val isLoading by viewModel.isLoading
    val isAuthor by viewModel.isAuthor.collectAsState()
    val comments = viewModel.comments.collectAsState()

    ScreenFrame(
        topBar = {
            TopBar(
                title = currentChapter?.chapterName ?: "Chapter",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    currentChapter?.content?.let { content ->
                        Text(
                            text = content,
                            color = Color.White,
                            fontSize = 15.sp,
                        )
                    } ?: Text(
                        text = "No content available",
                        color = Color.White,
                        fontSize = 15.sp,
                    )
                }

                // Push the end to the bottom
                Spacer(modifier = Modifier.weight(1f, fill = true))

                // Nút Next Chapter và Update Chapter (nếu là tác giả)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { viewModel.goToNextChapter() },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(vertical = 7.dp),
                        modifier = Modifier
                            .weight(if (isAuthor) 0.45f else 0.7f),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "Next Chapter",
                                color = Color.Black,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)),
                            )
                        }
                    }

                    if (isAuthor) {
                        Button(
                            onClick = {
                               viewModel.onGoToUpdateChapterScreen(viewModel.storyId.value,viewModel.chapterId.value)
                                      },
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            contentPadding = PaddingValues(vertical = 7.dp),
                            modifier = Modifier
                                .weight(0.45f),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = "Update Chapter",
                                    color = Color.Black,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght)),
                                )
                            }
                        }
                    }
                }

                TopComments(comments= comments.value, onClick ={})


            }
            Spacer(modifier = Modifier.height(24.dp))

            // Comment input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B4A4A), shape = RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scrollState = rememberScrollState()
                LaunchedEffect(comment) {
                    scrollState.scrollTo(scrollState.maxValue)
                }
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    decorationBox = { innerTextField ->
                        if (comment.isEmpty()) {
                            Text(
                                text = "Your Comment...",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                            )
                        }
                        innerTextField()
                    }
                )

                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add_img),
                        contentDescription = "Add Image",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}