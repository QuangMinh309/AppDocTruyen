package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard
import com.example.frontend.ui.components.TopBar


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
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Your Story",
                showBackButton = false,
                iconType = "Setting",
                onBackClick = { /*TODO*/ },
                onIconClick = { /*TODO*/ }
            )
        }
    ){
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* TODO */ },
                    containerColor = Color(0xFFFFAE95),
                    shape = RoundedCornerShape(50.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ), modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }
            },
            containerColor = Color.Black
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {

                Spacer(Modifier.height(20.dp))
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    items(stories) { story ->
                        StoryCard(
                            coverImage = painterResource(id = story[0] as Int),
                            title = story[1] as String,
                            author = "Penelope Marie-sn",
                            genres = (story[2] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            lastUpdated = story[3] as String,
                            views = story[4] as Int,
                            chapters = story[5] as Int
                        )
                    }
                }
            }
        }
    }
}
