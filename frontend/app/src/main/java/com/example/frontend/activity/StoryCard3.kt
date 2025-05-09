package com.example.frontend.activity

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.domain.StoryItemModel

data class StoryItem_Card3(
    val id: String,
    val title: String,
    val coverImage: String,
    val lastUpdated: String, // Format: "19/10/2024"
    val genres: List<String>, // ["Adventure", "Mystery", ...]
    val views: Long, // 167800
    val isPremium: Boolean = false
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoryCard3(
    story: StoryItemModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF2A2A2A)
//        )
//    ) {

    // Hàng 1: Ảnh bìa + Thông tin cơ bản
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ảnh bìa (tỉ lệ 3:4)
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = story.coverImage.takeIf { !it.isNullOrEmpty() } ?: R.drawable.placeholder_cover,
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


        }

        // Thông tin chi tiết
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tiêu đề
            Text(
                text = story.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ngày cập nhật
            Text(
                text = "Last Updated: ${story.lastUpdated}",
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Các thể loại (Chips)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                story.genres.forEach { genre ->
                    GenreChip(genre = genre)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.love_ic),
                    contentDescription = "Views",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatViews(story.views),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

}
//}


@Composable
fun GenreChip(genre: String) {
    Surface(
        color = Color.Unspecified,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(2.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00FF99),
                        Color(0xFF004099)  //
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = genre,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Preview
@Composable
fun chippreview()
{
    GenreChip("Kinh di")
}

// Định dạng số lượt xem (167800 -> 167.8K)
private fun formatViews(views: Long): String {
    return when {
        views >= 1000000 -> "${views / 1000000}M"
        views >= 1000 -> "${views / 1000}K"
        else -> views.toString()
    }
}

//@Preview
//@Composable
//fun StoryDetailCardPreview() {
//    val sampleStory = StoryItem_Card3(
//        id = "1",
//        title = "Natuvata Achemica",
//        coverImage = "https://example.com/cover.jpg",
//        lastUpdated = "19/10/2024",
//        genres = listOf("Adventure", "Mystery", "Autobiography"),
//        views = 167800,
//        isPremium = false
//    )
//
//    StoryDetailCard(
//        story = sampleStory,
//        modifier = Modifier.padding(16.dp)
//    )
//}