    package com.example.frontend.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.model.Story
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.StoryListViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.ExampleList
import okhttp3.internal.notifyAll


    @Composable
    @Preview
    fun PreViewStoryList(){
        val fakeviewmodel=StoryListViewModel(NavigationManager())
        ReadListScreen(fakeviewmodel)
    }

    @Composable


fun ReadListScreen(viewModel:StoryListViewModel= hiltViewModel()) {
    val listName="Listname"
        val storyItems= ExampleList

    ScreenFrame(
        topBar = {
            TopBar(
                title = listName,
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack()},
                onRightClick = {viewModel.onGoToSetting() }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {

            // (LazyColumn)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(storyItems) { story ->
                    StoryCard3(story = story, onClick = {viewModel.onGoToStoryScreen(1)})
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun StoryListScrennPreview()
//{
//    ReadListScreen("Litname",
//        ExampleList
//    )
//}