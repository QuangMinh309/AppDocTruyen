package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopComment(
    username: String,
    comment: String,
    chapter: String,
    date: String,
    likes: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "@$username", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = comment, color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$chapter\n$date", color = Color.Gray, fontSize = 12.sp)
                Text("👍 $likes", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
