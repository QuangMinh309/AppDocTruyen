package com.example.frontend.ui.screen.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.community.SearchingMemberViewModel
import com.example.frontend.ui.components.MemberCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBar


@Preview
@Composable
fun SearchingMemberScreen(viewModel: SearchingMemberViewModel = hiltViewModel()){
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val memberList = viewModel.memberList.collectAsState()
    ScreenFrame{
        //Search bar
        SearchBar(
            value = searchQuery.value,
            onValueChange = {searchQuery.value = it},
            cancelClick = {viewModel.onGoBack()}
        )

        // Members list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ){
            items(memberList.value.size){ index ->
                MemberCard(model = memberList.value[index])
            }

        }

    }
}