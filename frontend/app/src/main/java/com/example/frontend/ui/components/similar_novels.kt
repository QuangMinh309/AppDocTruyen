package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimilarNovels(novels: List<String>) {
    Column {
        Text("Novel tương tự", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            novels.forEach { title ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Blue, RoundedCornerShape(8.dp))
                    ) {
                        // Replace with Image when ready
                    }
                    Text(title, color = Color.White, fontSize = 12.sp)
                    Text("Mark mcallister", color = Color.Gray, fontSize = 10.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("💰 25.000đ", color = Color.White, fontSize = 10.sp)
                        Text("🔥 14.952", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
