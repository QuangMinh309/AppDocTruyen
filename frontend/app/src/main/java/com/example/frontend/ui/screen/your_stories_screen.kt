package com.example.frontend.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.R
import com.example.frontend.ui.components.StoryCard


@Preview
@Composable
fun YourStoryScreen() {
    // Fake data
    val stories = listOf(
        listOf(R.drawable.intro_page1_bg, "Naturata achemica", listOf("Adventure", "Mystery", "Autobiography"), "19/10/2024", 1006670, 34),
        listOf(R.drawable.story_detail_page1, "Puzzle Piece", listOf("Adventure", "Supernatural", "Autobiography"), "20/10/2024", 560000, 21),
        listOf(R.drawable.intro_page2_bg, "Tess of the Road", listOf("Adventure", "Mystery", "Autobiography", "Isekai"), "18/09/2024", 900000, 45),
        listOf(R.drawable.intro_page3_bg, "The Fourth Island", listOf("Adventure", "Mystery", "Autobiography"), "01/11/2024", 120300, 12)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = Color(0xFFFFAE95),
                shape = RoundedCornerShape(50.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }
        },
        containerColor = Color.Black
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "YOUR STORY",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght)),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painterResource(R.drawable.setting_icon),
                    contentDescription = "Setting Icon",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            Spacer(Modifier.height(15.dp))
            LazyColumn {
                items(stories) { story ->
                    StoryCard(
                        coverImage = painterResource(id = story[0] as Int),
                        title = story[1] as String,
                        author = "Penelope Marie-sn",
                        genres = story[2] as List<String>,
                        lastUpdated = story[3] as String,
                        views = story[4] as Int,
                        chapters = story[5] as Int
                    )
                }
            }
        }
    }
}
