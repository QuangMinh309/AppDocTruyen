package com.example.frontend.ui.screen.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.components.MemberCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBar


@Preview
@Composable
fun SearchingMemberScreen(){
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val memberList = listOf("member1","member2","member3","member4","member5")

    ScreenFrame(){
        //Search bar
        SearchBar(
            value = searchQuery.value,
            onValueChange = {searchQuery.value = it},
            cancelClick = {}
        )

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