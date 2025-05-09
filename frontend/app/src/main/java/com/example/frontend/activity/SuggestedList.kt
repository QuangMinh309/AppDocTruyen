package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreChips(genres: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2F1B24))
            .padding(16.dp)
    ) {
        // Hot pic title with fire icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Hot pic",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Thay bằng icon lửa nếu có
                contentDescription = "Fire Icon",
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp),
                tint = Color(0xFFFF5722) // Màu cam
            )
        }

        // Genre chips in a FlowRow to wrap content
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                GenreChip(genre = genre)
            }
        }
    }
}

//@Composable
//fun GenreChip(genre: String) {
//    Surface(
//        color = Color(0xFF3A3A3A),
//        shape = RoundedCornerShape(16.dp),
//        modifier = Modifier
//            .padding(2.dp)
//            .border(
//                width = 2.dp,
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFF00E676), // Màu xanh lá
//                        Color(0xFF2196F3)  // Màu xanh dương
//                    )
//                ),
//                shape = RoundedCornerShape(16.dp)
//            )
//    ) {
//        Text(
//            text = genre,
//            color = Color.White,
//            fontSize = 12.sp,
//            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
//        )
//    }
//}

@Preview
@Composable
fun GenreChipsPreview() {
    GenreChips(
        genres = listOf(
            "Things We Never Got Over",
            "Twisted Love",
            "Twisted Love",
            "Iron Flame",
            "Things We Never Got Over",
            "Things We Never Got Over",
            "Echoes of Eternity",
            "The Silent Storm",
            "Whispers in the Dark",
            "Fires of Redemption"
        )
    )
}