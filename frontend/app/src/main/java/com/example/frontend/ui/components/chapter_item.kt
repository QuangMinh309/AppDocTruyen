package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChapterItem(
    title: String,
    date: String,
    time: String,
    commentCount: String,
    viewCount: String,
    isLocked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Text(text = title, color = Color.White, fontSize = 14.sp)
                if (isLocked) {
                    Text(" 🔒", color = Color.Red, fontSize = 14.sp)
                }
            }
            Text("$date  $time", color = Color.Gray, fontSize = 12.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("💬 $commentCount", color = Color.Gray, fontSize = 12.sp)
            Text("⏱️ $viewCount", color = Color.Gray, fontSize = 12.sp)
        }
    }
}