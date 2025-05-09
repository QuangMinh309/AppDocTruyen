package com.example.frontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.HeaderBar
import com.example.frontend.ui.components.TopComments

@Preview
@Composable
fun ReadScreen() {
    var comment by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            HeaderBar()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your membership starts as soon as you set up payment and subscribe. Your monthly charge will occur on the last day of the current billing period." +
                        "We’ll renew your membership for you can manage your subscription or turn off auto-renewal under accounts setting.",
                color = Color.White,
                fontSize = 15.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Push the end to the bottom
            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = {},
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 70.dp, vertical = 7.dp),
                modifier = Modifier
                    .heightIn(39.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Chap tiếp theo",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val rawComments = listOf(
                listOf("huy", "Cảnh này chất!", R.drawable.intro_page3_bg, "Chap 3", "2025-05-06", "09:45", "24", "2"),
                listOf("thu", "Truyện hay nha", null, "Chap 1", "2025-05-05", "12:30", "33", "1")
            )
            TopComments(rawComments as List<List<Any>>)

            Spacer(modifier = Modifier.height(24.dp))

            // Comment input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B4A4A), shape = RoundedCornerShape(50))
                    .padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    decorationBox = { innerTextField ->
                        if (comment.isEmpty()) {
                            Text(
                                text = "Your Comment...",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                            )
                        }
                        innerTextField()
                    }
                )

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add_img),
                        contentDescription = "Add Image",
                        tint = Color.Black,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(13.dp))
        }
    }
}
