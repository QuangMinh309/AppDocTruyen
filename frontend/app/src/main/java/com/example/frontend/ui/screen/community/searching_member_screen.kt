package com.example.frontend.ui.screen.community

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.community.SearchingMemberViewModel
import com.example.frontend.ui.components.MemberCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SearchBar
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun SearchingMemberScreen(viewModel: SearchingMemberViewModel = hiltViewModel()){
    val searchQuery = viewModel.searchQuery.collectAsState()
    val memberList = viewModel.memberList.collectAsState()
    val isLoading =  viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val toast by viewModel.toast.collectAsState()

    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    ScreenFrame{
        //Search bar
        SearchBar(
            value = searchQuery.value,
            onValueChange = {viewModel.onSearchQueryChange(it)},
            cancelClick = {viewModel.onGoBack()}
        )

        // Members list
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OrangeRed)
            }

        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ){
                items(memberList.value.size){ index ->
                    val member = memberList.value[index]
                    MemberCard(
                        model = member,
                        onClick = {
                            viewModel.changeFollowState(member.id)
                        }
                    )
                }

            }
        }

    }
}