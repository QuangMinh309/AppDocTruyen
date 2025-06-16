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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.model.Category
import com.example.frontend.presentation.viewmodel.main_nav.StorySearchViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBarv2
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.StoryCard4
import com.example.frontend.ui.components.StoryChips


/*@Preview(showBackground = true)
@Composable
fun PreviewScreenContent3() {
    val fakeViewModel = StorySearchViewModel(NavigationManager())
    StorySearchScreen(viewModel = fakeViewModel)
}*/

@Composable
fun StorySearchScreen(viewModel: StorySearchViewModel = hiltViewModel()) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var selectedGenreTabIndex by remember { mutableIntStateOf(0) }
    var selectedStatusTabIndex by remember { mutableIntStateOf(0) }
    val categories = listOf(
        Category(id = 1, name = "Adventure"),
        Category(id = 2, name = "Autobiography"),
        Category(id = 3, name = "Mystery")
    )
    val statuses = listOf("Full", "Updated", "Premium", "Free")

    // Danh sách truyện lọc được từ backend (mock tạm thời)
    val filteredStories by remember { mutableStateOf(ExampleList) }

    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            // Gọi API từ viewModel để lọc truyện (mock tạm thời)
            // viewModel.searchStories(searchQuery.value).observeForever { stories ->
            //     filteredStories = stories
            // }
        }
    }

    ScreenFrame(
        topBar = {
            SearchBarv2(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                isSearching = isSearching,
                onSearchClick = { isSearching = true },
                onCancel = {
                    isSearching = false
                    searchQuery.value = ""
                },
                onSettingClick = { viewModel.onGoToSetting() }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            if (!isSearching) {
                TabRow(
                    selectedTabIndex = selectedGenreTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ) {
                    categories.forEachIndexed { index, genre ->
                        Tab(
                            selected = selectedGenreTabIndex == index,
                            onClick = { selectedGenreTabIndex = index },
                            text = { Text(text = genre.name?:"", fontSize = 12.sp) },
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
                            onClick = { selectedStatusTabIndex = index },
                            text = { Text(text = status, fontSize = 12.sp) },
                            selectedContentColor = Color(0xFFFF9900),
                            unselectedContentColor = Color.LightGray
                        )
                    }
                }

//                LazyColumn(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(8.dp)
//                ) {
//                    items(
//                        if (selectedStatusTabIndex > 1) {
//                            getSampleStories(
//                                category = categories[selectedGenreTabIndex],
//                                status = "all",
//                                premiumStatus = statuses[selectedStatusTabIndex]
//                            )
//                        } else {
//                            getSampleStories(
//                                category = categories[selectedGenreTabIndex],
//                                status = "all",
//                                premiumStatus = statuses[selectedStatusTabIndex]
//                            )
//                        }
//                    ) { story ->
//                        StoryCard4(story = story, onClick = { viewModel.onGoToStoryScreen(story.id) })
//                    }
//                }
            } else {
                if (searchQuery.value.isEmpty()) {
                    StoryChips(
                        texts = categories,
                        viewModel = viewModel,
//                        modifier = Modifier.
//                                padding(vertical = 40.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredStories) { story ->
                            StoryCard3(story = story, onClick = { viewModel.onGoToStoryScreen(story.id) })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

