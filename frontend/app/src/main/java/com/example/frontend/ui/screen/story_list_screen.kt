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
import com.example.frontend.domain.StoryItemModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryCard3
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.ExampleList

    @Composable
fun ReadListScreen(listName: String, storyItems: List<StoryItemModel>) {
    ScreenFrame(
        topBar = {
            TopBar(
                title = listName,
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
                .padding(top = 20.dp)
        ) {

            // (LazyColumn)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(storyItems) { story ->
                    StoryCard3(story = story)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun StoryListScrennPreview()
{
    ReadListScreen("Litname",
        ExampleList
    )
}