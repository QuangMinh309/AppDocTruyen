package com.example.frontend.ui.screen.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.TopComments

@Preview
@Composable
fun ReadScreen() {
    var comment by remember { mutableStateOf("") }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "ChapterName",
                showBackButton = true,
                iconType = "Setting",
                onBackClick = { /*TODO*/ },
                onIconClick = { /*TODO*/ }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Your membership starts as soon as you set up payment and subscribe. Your monthly charge will occur on the last day of the current billing period." +
                        "We’ll renew your membership for you can manage your subscription or turn off auto-renewal under accounts setting.",
                color = Color.White,
                fontSize = 15.sp,
            )

            // Push the end to the bottom
            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = {},
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues( vertical = 7.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Next Chapter",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val rawComments = listOf(
                listOf("huy", "Cảnh này chất!", R.drawable.intro_page1_bg, "Chap 3", "2025-05-06", "09:45", "24", "2"),
                listOf("huy", null, R.drawable.intro_page1_bg, "Chap 3", "2025-05-06", "09:45", "24", "2"),
                listOf("thu", "Truyện hay nha", null, "Chap 1", "2025-05-05", "12:30", "33", "1")
            )
            TopComments(rawComments.filterIsInstance<List<Any>>())

            Spacer(modifier = Modifier.height(24.dp))

            // Comment input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B4A4A), shape = RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scrollState = rememberScrollState()
                LaunchedEffect(comment) {
                    scrollState.scrollTo(scrollState.maxValue)
                }
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
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(13.dp))
        }
    }
}
