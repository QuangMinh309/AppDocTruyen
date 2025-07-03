    package com.example.frontend.ui.screen.community


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.community.ChattingViewModel
import com.example.frontend.ui.components.ChatBubble
import com.example.frontend.ui.components.MyChatBubble
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed


    @Composable
fun ChattingScreen(viewModel: ChattingViewModel = hiltViewModel())
{
    val communityId = viewModel.id.collectAsState()
    val yourChat by viewModel.yourChat.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val messages = viewModel.messages.collectAsState()
    val toast by viewModel.toast.collectAsState()
    val commentUri by viewModel.selectedPicUri.collectAsState()

    val context = LocalContext.current
    val listState = rememberLazyListState()

    var showImagePicker by remember { mutableStateOf(false) }

    // Launcher để chọn ảnh từ gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.setCommentUri(uri)
            }
            showImagePicker = false
        }
    )
    //cuộn xuống dưới khi có tin nhắn mới
    LaunchedEffect(messages.value) {
        if (messages.value.isNotEmpty()) {
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }

    //toast thông báo chung
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    ScreenFrame(
        topBar = {
            TopBar(
//                title = name ,
                showBackButton = true,
                iconType = "Searching",
                onLeftClick = {
                    viewModel.back()
                   },
                onRightClick = {
                    viewModel.onGoToSearchingMemberScreen(communityId.value.toInt())
                }
            )
        }
    ){
        //chatting
        Box (
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
            ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeRed)
                }
            }
            else{
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 90.dp)
                        .padding(top = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(45.dp)
                ) {
                    items(messages.value.size) { index ->
                        if (messages.value[index].isUser) {
                            MyChatBubble(message = messages.value[index])
                        } else {
                            ChatBubble(message = messages.value[index])
                        }

                    }
                }
                // chat input field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .background(Color(0xFF4B4A4A), shape = RoundedCornerShape(50))
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val scrollState = rememberScrollState()
                    LaunchedEffect(yourChat) {
                        scrollState.scrollTo(scrollState.maxValue)
                    }

                    commentUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(60.dp)
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                        )
                    }
                    BasicTextField(
                        value = yourChat,
                        onValueChange = {  viewModel.updateChat(it) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        textStyle = TextStyle(color = Color.White),
                        decorationBox = { innerTextField ->
                            if (yourChat.isEmpty()) {
                                Text(
                                    text = "Your Comment...",
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }
                            innerTextField()
                        }
                    )

                    IconButton(
                        onClick = {
                            showImagePicker = true
                        }
                    ) {
                        Icon(
                            painter =  painterResource(id = R.drawable.icon_add_img) ,
                            contentDescription = "Add Image",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(28.dp),
                        )
                    }

                    IconButton(
                        onClick = {
                            if(yourChat != "" || (commentUri != null))
                                viewModel.createChat(context)
                        }
                    ) {
                        Icon(

                            painter = painterResource(id = R.drawable.popular_icon),
                            contentDescription = "Add Image",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(28.dp),

                            )
                    }
                }
            }


        }
    }

    // Mở picker khi showImagePicker = true
    if (showImagePicker) {
        launcher.launch("image/*")
    }
}