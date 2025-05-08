package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

@Composable
fun AuthorInfo(authorName: String, username: String, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.avt_img),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .heightIn(69.dp)
                .widthIn(69.dp)
                .padding(end = 19.dp)
        )
        Column {
            Text(
                text = authorName,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 7.dp)
            )
            Text(text = "@$username", color = Color.White, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onMoreClick) {
            Text("Thêm >", color = Color.White)
        }
    }
}
