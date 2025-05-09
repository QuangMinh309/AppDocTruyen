package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.domain.StoryItemModel

data class StoryItem(
    val id: String,
    val title: String,
    val author: String,
    val coverImage: String, // URL hoặc resource ID
    val likes: Int,
    val chapters: Int,
    val isPremium: Boolean
)

@Composable
fun HorizontalStoryCard(
    story: StoryItemModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp) // Chiều cao cố định cho card ngang
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(8.dp))
    ) {
        // Hình ảnh bìa truyện (tỉ lệ 3:4)
        Box(
            modifier = Modifier
                .width(90.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(6.dp))
        ) {
            AsyncImage(
                model = story.coverImage.takeIf { !it.isNullOrEmpty() } ?: R.drawable.placeholder_cover,
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.placeholder_cover),
                error = painterResource(R.drawable.broken_image)
            )
        }

        // Phần thông tin bên phải
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f)
        ) {
            // Dòng tiêu đề + icon khóa
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = story.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                if (story.isPremium) {
                    Icon(
                        painter = painterResource(R.drawable.lock_ic),
                        contentDescription = "Premium",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 4.dp)
                    )

                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tác giả
            Text(
                text = "@${story.author}",
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thống kê
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Lượt thích
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.love_ic),
                        contentDescription = "Likes",
                        tint = Color(0xFFFF4081),
                        modifier = Modifier.size(20.dp))

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${story.likes}",
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp
                    )
                }

                // Số chapter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.chapternumber_ic),
                        contentDescription = "Chapters",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp))

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${story.chaptersnumbers} ch",
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun HorizontalStoryCardPreview() {
//    val stories = listOf(
//        StoryItem(
//            id = "1",
//            title = "Songs of Conquest",
//            author = "penelopes",
//            coverImage = "https://example.com/cover1.jpg",
//            likes = 5200,
//            chapters = 32,
//            isPremium = true
//        ),
//        StoryItem(
//            id = "2",
//            title = "The Midnight Library",
//            author = "matt_haig",
//            coverImage = "https://example.com/cover2.jpg",
//            likes = 3200,
//            chapters = 28,
//            isPremium = false
//        )
//    )
//
//    Column(
//        modifier = Modifier
//            .background(Color(0xFF121212))
//            .padding(16.dp)
//    ) {
//        stories.forEach { story ->
//            HorizontalStoryCard(story = story)
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//    }
//}