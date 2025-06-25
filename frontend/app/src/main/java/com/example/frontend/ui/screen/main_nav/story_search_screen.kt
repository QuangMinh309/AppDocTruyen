package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val statuses = listOf("update", "full", "pending", "rejected","approved")

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
                TabRow(
                    selectedTabIndex = selectedGenreTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ) {
                    categories.forEachIndexed { index, genre ->
                        Tab(
                            selected = selectedGenreTabIndex == index,
                            onClick = { viewModel.onSelectedGenreTabIndexChange(index) },
                            text = { Text(text = genre.name ?: "", fontSize = 12.sp) },
                            selectedContentColor = Color(0xFFFF9900),
                            unselectedContentColor = Color.LightGray
                        )
                    }
                }

                TabRow(
                    selectedTabIndex = selectedStatusTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ) {
                    statuses.forEachIndexed { index, status ->
                        Tab(
                            selected = selectedStatusTabIndex == index,
                            onClick = { viewModel.onSelectedStatusTabIndexChange(index) },
                            text = { Text(text = status, fontSize = 12.sp) },
                            selectedContentColor = Color(0xFFFF9900),
                            unselectedContentColor = Color.LightGray
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(stories) { story ->
                        StoryCard4(story = story) {  }
                    }
                }
            } else {
                if (searchQuery.isEmpty()) {
                    StoryChips(
                        texts = categories,
                        viewModel = viewModel
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(stories) { story ->
                            StoryCard3(story = story) { }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}