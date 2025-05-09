package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun StoryCard(
    story: StoryItemModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Hình ảnh bìa truyện
            AsyncImage(
                model = story.coverImage.takeIf { !it.isNullOrEmpty() } ?: R.drawable.placeholder_cover,
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(R.drawable.placeholder_cover),
                error = painterResource(R.drawable.placeholder_cover)
            )

            // Nhãn Premium/Free
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = if (story.isPremium) Color(0xFFFFD700) else Color(0xFF4CAF50),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (story.isPremium) "PREMIUM" else "FREE",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tiêu đề truyện
        Text(
            text = story.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Tác giả
        Text(
            text = "@${story.author}",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Mô tả ngắn
        Text(
            text = story.shortDescription,
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}

//@Preview
//@Composable
//fun StoryCardPreview() {
//    val premiumStory = StoryItemModel(
//        id = "1",
//        title = "I Want a Better Catastrophe",
//        author = "Andrew Boyd",
//        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//        isPremium = true,
//        shortDescription = "Navigating the Climate Crisis with Grief, Hope, and Gallows Humor"
//    )
//
//    val freeStory = StoryItemModel(
//        id = "2",
//        title = "The Midnight Library",
//        author = "Matt Haig",
//        coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
//        isPremium = false,
//        shortDescription = "A novel about all the choices that go into a life well lived"
//    )
//
//    Row(
//        modifier = Modifier
//            .background(Color(0xFF121212))
//            .padding(16.dp)
//    ) {
//        StoryCard(story = premiumStory)
//        Spacer(modifier = Modifier.width(16.dp))
//        StoryCard(story = freeStory)
//    }
//}