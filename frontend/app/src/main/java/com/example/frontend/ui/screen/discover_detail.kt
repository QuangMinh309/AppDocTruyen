package com.example.frontend.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.StoryChips

@Preview
@Composable
fun DiscoverDetail()
{
    val searchQuery = rememberSaveable { mutableStateOf("") }
    ScreenFrame {
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(20.dp))
            //Search bar
            com.example.frontend.ui.components.SearchBar(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                cancelClick = {}
            )
            StoryChips(
                texts = listOf(
                "Things We Never Got Over",
                "Twisted Love",
                "Twisted Love",
                "Iron Flame",
                "Things We Never Got Over",
                "Things We Never Got Over",
                "Echoes of Eternity",
                "The Silent Storm",
                "Whispers in the Dark",
                "Fires of Redemption"
                ),
                modifier = Modifier.padding(vertical = 40.dp)
            )


        }
    }
}
