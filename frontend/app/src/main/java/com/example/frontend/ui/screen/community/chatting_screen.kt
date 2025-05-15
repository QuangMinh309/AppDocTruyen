    package com.example.frontend.ui.screen.community


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.community.ChattingViewModel
import com.example.frontend.ui.components.ChatBubble
import com.example.frontend.ui.components.MyChatBubble
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.demoAppUser
import com.example.frontend.ui.screen.main_nav.demoChatList

@Preview(showBackground = true)
@Composable
fun PreviewScreenContent() {
    val fakeViewModel =ChattingViewModel (NavigationManager())
    ChattingScreen(viewModel = fakeViewModel)
}

@Preview
@Composable
fun ChattingScreen(viewModel: ChattingViewModel = hiltViewModel())
{
    val yourChat by viewModel.yourChat.collectAsState()

    val messages = demoChatList

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Food in anime",
                showBackButton = true,
                iconType = "Searching",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSearchingMemberScreen(viewModel.communityId) }
            )
        }
    ){
        //chatting
        Column (Modifier.fillMaxWidth(),
                 horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                verticalArrangement = Arrangement.spacedBy(45.dp)
            ) {
                items(messages.size) { index ->
                    if (demoChatList[index].sender.id == demoAppUser.id) {
                        MyChatBubble(message = demoChatList[index])
                    } else {
                        ChatBubble(message = demoChatList[index])
                    }

                }
            }
            Spacer(Modifier.weight(1f))
            // chat input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B4A4A), shape = RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scrollState = rememberScrollState()
                LaunchedEffect(yourChat) {
                    scrollState.scrollTo(scrollState.maxValue)
                }
                BasicTextField(
                    value = yourChat,
                    onValueChange = {  viewModel.updateChat(it) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    decorationBox = { innerTextField ->
                        if (yourChat.isEmpty()) {
                            Text(
                                text = "Your Comment...",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                            )
                        }
                        innerTextField()
                    }
                )

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = if(yourChat == "") painterResource(id = R.drawable.icon_add_img) else painterResource(id = R.drawable.popular_icon),
                        contentDescription = "Add Image",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}