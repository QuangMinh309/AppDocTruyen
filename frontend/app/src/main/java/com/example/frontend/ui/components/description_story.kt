package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionStory(
    aboutContent: @Composable () -> Unit,
    chapterContent: @Composable () -> Unit
) {
    var selectedTab by remember { mutableStateOf(1) }
    val tabTitles = listOf("About", "Chapter")

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                ) {
                    Text(title, modifier = Modifier.padding(8.dp))
                }
            }
        }

        when (selectedTab) {
            0 -> aboutContent()
            1 -> chapterContent()
        }
    }
}
