package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun AuthorInfo(authorName: String, username: String, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = authorName, color = Color.White, fontSize = 16.sp)
            Text(text = "@$username", color = Color.Gray, fontSize = 12.sp)
        }
        TextButton(onClick = onMoreClick) {
            Text("Thêm >", color = Color.White)
        }
    }
}
