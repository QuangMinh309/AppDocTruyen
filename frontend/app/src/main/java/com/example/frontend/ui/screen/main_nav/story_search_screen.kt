package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.main_nav.StorySearchViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBarv2
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.StoryCard4
import com.example.frontend.ui.components.StoryChips

@Composable
fun StorySearchScreen(viewModel: StorySearchViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearching by remember { mutableStateOf(false) }
    val selectedGenreTabIndex by viewModel.selectedGenreTabIndex.collectAsState()
    val selectedStatusTabIndex by viewModel.selectedStatusTabIndex.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val statuses = listOf("update", "full")

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isSearching = true
        } else {
            isSearching = false
        }
    }

    ScreenFrame(
        topBar = {
            SearchBarv2(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                isSearching = isSearching,
                onSearchClick = { isSearching = true },
                onCancel = {
                    isSearching = false
                    viewModel.onSearchQueryChange("")
                },
                onSettingClick = { viewModel.onGoToSetting() }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))

            if (!isSearching && categories.isNotEmpty()) {
                // Genre tabs with LazyRow
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { genre ->
                        val index = categories.indexOf(genre)
                        Text(
                            text = genre.name ?: "",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedGenreTabIndex == index) Color(0xFFFF9900) else Color.White
                            ),
                            modifier = Modifier
                                .background(
                                    color = if (selectedGenreTabIndex == index) Color.Black.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { viewModel.onSelectedGenreTabIndexChange(index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Status tabs with Row (centered)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    statuses.forEachIndexed { index, status ->
                        Text(
                            text = status,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedStatusTabIndex == index) Color(0xFFFF9900) else Color.White
                            ),
                            modifier = Modifier
                                .background(
                                    color = if (selectedStatusTabIndex == index) Color.Black.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { viewModel.onSelectedStatusTabIndexChange(index) }
                        )
                        if (index < statuses.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(stories) { story ->
                        StoryCard4(story = story, onClick = { viewModel.onGoToStoryScreen(story.id) })
                    }
                }
            } else {
                if (searchQuery.isEmpty()) {
                    StoryChips(
                        texts = categories,
                        onClick = { id, name -> viewModel.onGoToCategoryStoryList(id, name) }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(stories) { story ->
                            StoryCard3(story = story, onClick ={ viewModel.onGoToStoryScreen(story.id)})
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}