package com.example.frontend.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun Chip(text: String) {
    Surface(
        color = Color.Unspecified,
        shape = RoundedCornerShape(30.dp),
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
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StoryChips(modifier: Modifier = Modifier,texts: List<String>) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Hot pic title with fire icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = "Hot pic",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            repeat(times = 3) { index ->
                Icon(
                    painter = painterResource(id = R.drawable.popular_icon), // Thay bằng icon lửa nếu có
                    contentDescription = "Fire Icon",
                    modifier = Modifier
                        .size(18.dp)
                        .padding(start = 4.dp),
                    tint = OrangeRed
                )
            }

        }

        // chips in a FlowRow to wrap content
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            texts.forEach { genre ->
                Chip(text = genre)
            }
        }
    }
}


