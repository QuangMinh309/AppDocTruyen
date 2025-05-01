package com.example.frontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.AuthorInfo
import com.example.frontend.ui.components.ChapterItem
import com.example.frontend.ui.components.DescriptionStory
import com.example.frontend.ui.components.GenreTags
import com.example.frontend.ui.components.HeaderBar
import com.example.frontend.ui.components.NovelInfo
import com.example.frontend.ui.components.SimilarNovels
import com.example.frontend.ui.components.StoryStatusAction
import com.example.frontend.ui.components.TopComment
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed

@Preview
@Composable
fun NovelDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        HeaderBar()
        Spacer(Modifier.height(8.dp))
        NovelInfo()
        Spacer(Modifier.height(8.dp))
        StoryStatusAction(true, false, "Updating", {})
        Spacer(Modifier.height(8.dp))
        DescriptionStory(
            aboutContent = {
                Text(
                    text = "Your membership starts as soon as you set up payment and subscribe. Your monthly charge will occur on the last day of the current billing period...",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                AuthorInfo(authorName = "PeneLoped Lynne", username = "tolapenelope") { }
                GenreTags()
                Text(
                    text = "Top Comment",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
                TopComment("tolapenelope", "Lorem ipsum dolor sit amet, consec tetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim.",
                    "Chapter3", " 19/10/2024   09:00", "23K")
                SimilarNovels(listOf(
                    "Demon Slayer",
                    "One Piece",
                    "Attack on Titan",
                    "My Hero Academia",
                    "Jujutsu Kaisen"
                ))
            },
            chapterContent = {
                ChapterItem(
                    title = "Chapter1",
                    date = "2025-05-01",
                    time = "10:00 AM",
                    commentCount = "120",
                    viewCount = "500",
                    isLocked = false
                )
                ChapterItem(
                    title = "Chapter2",
                    date = "2025-05-02",
                    time = "12:30 PM",
                    commentCount = "80",
                    viewCount = "350",
                    isLocked = true
                )
                ChapterItem(
                    title = "Chapter3",
                    date = "2025-05-03",
                    time = "03:00 PM",
                    commentCount = "200",
                    viewCount = "1000",
                    isLocked = false
                )
                Button(
                    onClick = {},
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(41.dp)
                        .padding(start = 7.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(OrangeRed, BurntCoral),
                                endX = 250f
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 29.dp)
                ) {
                    Text(
                        text = "Start read",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                    )
                }
            }
        )
        Spacer(Modifier.height(8.dp))
    }
}