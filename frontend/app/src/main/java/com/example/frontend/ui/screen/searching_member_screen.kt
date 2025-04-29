package com.example.frontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.components.MemberCard
import com.example.frontend.ui.components.SearchBar
import com.example.frontend.ui.theme.DeepSpace


@Preview
@Composable
fun SearchingMemberScreen(){
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val memberList = listOf("member1","member2","member3","member4","member5")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .padding(
                WindowInsets.statusBars
                    .asPaddingValues()
            )
            .padding(16.dp)

    ){
        //Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            SearchBar(
                modifier = Modifier
                    .weight(0.8f),
                value = searchQuery.value,
                onValueChange = {searchQuery.value = it}
            )

            Text(
                text = "Cancel",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }

        // Members list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ){
            items(memberList){item->
                MemberCard(
                    item = item
                )
            }

        }

    }
}