package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

//data class ReadListItem(
//    val id: String,
//    val name: String,
//    val date: String,
//    val description: String,
//    val stories: List<StoryItem_Card3> // Danh sách truyện thay vì avatar
//)

@Composable
fun ReadListItem(
    item: ReadListItemModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(12.dp),
////        colors = CardDefaults.cardColors(
////            containerColor = Color(0xFFF5F5F5)
////        )
//    ) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Phần avatar xếp chồng (lấy từ 3 truyện đầu tiên)
        Box(
            modifier = Modifier.size(100.dp)
        ) {
            item.stories.take(3).forEachIndexed { index, story ->
                AsyncImage(
                    model = story.coverImage, // Lấy avatar từ coverImage của truyện
                    contentDescription = "Story cover ${index + 1}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(80.dp)
                        .width(60.dp)

                        //   .clip(CircleShape)
                        .align(Alignment.TopStart)
                        .offset(
                            x = (index * 10).dp,
                            y = (index * 10).dp
                        )
                        .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
//                            .background(Color.White, CircleShape)
                        .padding(2.dp)
                )
            }

            // Hiển thị số truyện còn lại nếu có
//                if (item.stories.size > 3) {
//                    Box(
//                        modifier = Modifier
//                            .size(50.dp)
//                            .clip(CircleShape)
//                            .background(Color.LightGray)
//                            .align(Alignment.TopStart)
//                            .offset(x = 40.dp, y = 40.dp)
//                            .padding(2.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "+${item.stories.size - 3}",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
        }

        // Phần thông tin
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.name,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFF76800), // Màu xanh lam đậm
                                Color(0xFFDF4258)  // Màu xanh lam nhạt hơn
                            )
                        ),
                        shape = RoundedCornerShape(30.dp) // Bo góc 8dp
                    )
//                        .border(
//                            width = 2.dp,
//                            brush = Brush.linearGradient(
//                                colors = listOf(
//                                    Color(0xFF00E676), // Màu xanh lá
//                                    Color(0xFF2196F3)  // Màu xanh dương
//                                )
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Text(
                text = item.date,
                color = Color(0xFFF76800),
                fontSize = 12.sp
            )

            Text(
                text = item.description,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2
            )
        }

        // Nút "Show full list"
//            TextButton(
//                onClick = onClick,
//                modifier = Modifier.align(Alignment.Bottom)
//            ) {
//                Text(
//                    text = "Show full list >",
//                    color = Color(0xFF6200EE),
//                    fontSize = 8.sp
//                )
//            }
    }
}
//}

@Preview
@Composable
fun ReadListItemPreview() {
    val sampleStories = listOf(
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120

        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://baodongnai.com.vn/file/e7837c02876411cd0187645a2551379f/022024/18_1_20240229171501.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSb3Fs752Mx1GL4D6g6EpDw4uPms47KDqDdvQ&s",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI3OOV4uJq_HKA2ReFpTaTT7y0eo7bQ3Hr7Q&s",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        ),
        StoryItemModel(
            id="1",
            title = "Alibaba",
            coverImage = "https://photo.znews.vn/w660/Uploaded/ngogtn/2020_10_20/avatar_thenextshadow_comiccover.jpg",
            shortDescription = "",
            isPremium = true,
            author = "Hhahaa",
            likes = 100,
            chaptersnumbers = 10,
            genres = listOf("Adventure","Mystery","Autobiography"),
            lastUpdated = "22/12/2024",
            views = 120
        )
    )

    val sampleItem = ReadListItemModel(
        id = "1",
        name = "ReadList number 1",
        date = "11/11/2025...",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim.",
        stories = sampleStories
    )

    ReadListItem(
        item = sampleItem,
        modifier = Modifier.padding(16.dp)
    )
}