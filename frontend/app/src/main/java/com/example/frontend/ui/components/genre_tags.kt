package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun GenreTags() {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        listOf(
            "Adventure", "Mystery", "Autobiography", "Fantasy", "Drama",
        ).forEach { tag ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFFF9800), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(tag, color = Color.Black, fontSize = 12.sp)
            }
        }
    }
}
