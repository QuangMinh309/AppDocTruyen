package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.domain.StoryItemModel

@Composable
fun StoryCard2(
    story: StoryItemModel,
    modifier: Modifier = Modifier,
    onStoryClick: (StoryItemModel) -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(200.dp)
            .background(Color(0xFF2F1B24), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Story Image (on top)
        AsyncImage(
            model = story.coverImage,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Story Details (below the image)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = story.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (story.isPremium) { // Chỉ hiển thị biểu tượng khóa nếu truyện là premium
                Icon(
                    painter = painterResource(id = R.drawable.lock_ic), // Biểu tượng khóa
                    contentDescription = "Lock",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFFFF5722) // Màu cam
                )
            }
        }
        Text(
            text = "@${story.author}",
            color = Color(0xFFFF5722), // Màu cam
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.love_ic), // Biểu tượng trái tim
                contentDescription = "Likes",
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFFF5722) // Màu cam
            )
            Text(
                text = "${story.likes}",
                color = Color(0xFFFF5722), // Màu cam
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.chapternumber_ic), // Biểu tượng danh sách
                contentDescription = "Chapters",
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFFF5722) // Màu cam
            )
            Text(
                text = "${story.chaptersnumbers}",
                color = Color(0xFFFF5722), // Màu cam
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun StoryCardtestPreview() {
    Column {
        StoryCard2(
            story = StoryItemModel(
                id = "1",
                title = "Alibaba",
                coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
                shortDescription = "",
                isPremium = true,
                author = "Hhahaa",
                likes = 100,
                chaptersnumbers = 10,
                genres = listOf("Adventure", "Mystery", "Autobiography"),
                lastUpdated = "22/12/2024",
                views = 120
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        StoryCard2(
            story = StoryItemModel(
                id = "1",
                title = "Alibaba",
                coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
                shortDescription = "",
                isPremium = true,
                author = "Hhahaa",
                likes = 100,
                chaptersnumbers = 10,
                genres = listOf("Adventure", "Mystery", "Autobiography"),
                lastUpdated = "22/12/2024",
                views = 120
            )
        )
    }
}