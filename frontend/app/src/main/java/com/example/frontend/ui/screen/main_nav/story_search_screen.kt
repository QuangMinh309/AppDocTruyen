package com.example.frontend.ui.screen.main_nav

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBarv2

@Composable
fun StorySearchScreen() {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    var selectedGenreTabIndex by remember { mutableIntStateOf(0) }
    var selectedStatusTabIndex by remember { mutableIntStateOf(0) }
    val genres = listOf("Adventure", "Romantic", "Fantastic", "Discovery")
    val statuses = listOf("đã full", "đang cập nhật", "truyện phí", "miễn phí")

    ScreenFrame(
        topBar = {
            SearchBarv2(
                value = searchQuery.value,
                onValueChange = {searchQuery.value = it},
                onClick = {}
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //   SearchBar
            Spacer(modifier = Modifier.height(20.dp))

            // Genre Tab Row
            TabRow(
                selectedTabIndex = selectedGenreTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.White
            ) {
                genres.forEachIndexed { index, genre ->
                    Tab(
                        selected = selectedGenreTabIndex == index,
                        onClick = { selectedGenreTabIndex = index },
                        text = { Text(text=genre, fontSize = 12.sp) },
                        selectedContentColor = Color(0xFFFF9900),
                        unselectedContentColor = Color.LightGray
                    )
                }
            }

            // Status Tab Row
            TabRow(
                selectedTabIndex = selectedStatusTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.White
            ) {
                statuses.forEachIndexed { index, status ->
                    Tab(
                        selected = selectedStatusTabIndex == index,
                        onClick = { selectedStatusTabIndex = index },
                        text = { Text(text=status, fontSize = 12.sp) },
                        selectedContentColor = Color(0xFFFF9900),
                        unselectedContentColor = Color.LightGray
                    )
                }
            }

            // Story List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(getSampleStories(selectedGenreTabIndex, selectedStatusTabIndex)) { story ->
                    StoryItem(story = story)
                }
            }
        }
    }
}

@Composable
fun StoryItem(story: Story) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF3C2F2F), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Story Image
        AsyncImage(
            model = story.imageUrl,
            contentDescription = "Story Image",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Story Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = android.R.drawable.star_big_on),
                    contentDescription = "Crown",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFFFD700)
                )
                Text(
                    text = story.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(
                text = "Last Updated: ${story.lastUpdated}",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Row {
                story.genres.forEach { genre ->
                    Text(
                        text = genre,
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .background(Color(0xFF2E7D32), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = "${story.views}K",
                color = Color(0xFFFFA500),
                fontSize = 12.sp
            )
        }
    }
}

data class Story(
    val title: String,
    val imageUrl: String,
    val lastUpdated: String,
    val genres: List<String>,
    val views: Int,
    val genreIndex: Int,
    val statusIndex: Int
)

fun getSampleStories(genreIndex: Int, statusIndex: Int): List<Story> {
    return listOf(
        Story(
            title = "Naturata achemi",
            imageUrl = "https://example.com/story1.jpg",
            lastUpdated = "18/10/2024",
            genres = listOf("Adventure", "Mystery", "Autobiography"),
            views = 167,
            genreIndex = 0,
            statusIndex = 1
        ),
        Story(
            title = "Naturata achemi",
            imageUrl = "https://example.com/story2.jpg",
            lastUpdated = "18/10/2024",
            genres = listOf("Romantic", "Mystery", "Autobiography"),
            views = 167,
            genreIndex = 1,
            statusIndex = 0
        ),
        Story(
            title = "Naturata achemi",
            imageUrl = "https://example.com/story3.jpg",
            lastUpdated = "18/10/2024",
            genres = listOf("Fantastic", "Mystery", "Autobiography"),
            views = 167,
            genreIndex = 2,
            statusIndex = 2
        ),
        Story(
            title = "Naturata achemi",
            imageUrl = "https://example.com/story4.jpg",
            lastUpdated = "18/10/2024",
            genres = listOf("Discovery", "Mystery", "Autobiography"),
            views = 167,
            genreIndex = 3,
            statusIndex = 3
        )
    ).filter { it.genreIndex == genreIndex && it.statusIndex == statusIndex }
}

@Preview
@Composable
fun StoryNavigationPreview() {
    MaterialTheme {
        StorySearchScreen()
    }
}