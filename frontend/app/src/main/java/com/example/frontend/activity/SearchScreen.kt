package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun StoryNavigation() {
    var selectedGenreTabIndex by remember { mutableIntStateOf(0) }
    var selectedStatusTabIndex by remember { mutableIntStateOf(0) }
    val genres = listOf("Adventure", "Romantic", "Fantastic", "Discovery")
    val statuses = listOf("đã full", "đang cập nhật", "truyện phí", "miễn phí")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2F1B24))
    ) {
        //   SearchBar
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchBar(modifier = Modifier.weight(1f) )
            Icon(
                painter = painterResource(id = R.drawable.setting_ic),
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)  // Giảm kích thước icon
                    .clickable { /* Xử lý click */ }
            )
        }
        //    SearchBar()

        // Genre Tab Row
        TabRow(
            selectedTabIndex = selectedGenreTabIndex,
            containerColor = Color(0xFF2F1B24),
            contentColor = Color.White
        ) {
            genres.forEachIndexed { index, genre ->
                Tab(
                    selected = selectedGenreTabIndex == index,
                    onClick = { selectedGenreTabIndex = index },
                    text = { Text(text=genre, fontSize = 10.sp) },
                    selectedContentColor = Color(0xFFFF9900),
                    unselectedContentColor = Color.Gray
                )
            }
        }

        // Status Tab Row
        TabRow(
            selectedTabIndex = selectedStatusTabIndex,
            containerColor = Color(0xFF2F1B24),
            contentColor = Color.White
        ) {
            statuses.forEachIndexed { index, status ->
                Tab(
                    selected = selectedStatusTabIndex == index,
                    onClick = { selectedStatusTabIndex = index },
                    text = { Text(text=status, fontSize = 10.sp) },
                    selectedContentColor = Color(0xFFFF9900),
                    unselectedContentColor = Color.Gray
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
@Composable
fun SearchBar(
    hint: String = "find story ...",
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Color(0x20FFFFFF),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search_navigation_ic),
            contentDescription = "Search icon",
            tint = Color(0xFFFF9900),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(it)
            },
            placeholder = {
                Text(
                    text = hint,
                    color = Color(0xFFBDBDBD),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(searchText) }
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            singleLine = true,
            shape = RoundedCornerShape(28.dp)
        )
    }
}
@Preview
@Composable
fun StoryNavigationPreview() {
    MaterialTheme {
        StoryNavigation()
    }
}