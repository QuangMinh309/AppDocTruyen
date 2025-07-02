package com.example.frontend.ui.screen


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.NotificationViewModel

import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar

@Composable
fun NotificationScreen(viewModel: NotificationViewModel= hiltViewModel()){
    val notification = viewModel.notifications.collectAsState()

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Notification",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        LazyColumn (
            modifier = Modifier
                .padding(vertical = 40.dp)
        ){
            items(notification.value.size) { index ->
                Box(
                    modifier = Modifier.pointerInput(Unit) {
                    }
                ) {
                    NotificationCard(
                        content = notification.value[index].content,
                        type = notification.value[index].type,
                        time = notification.value[index].createAt
                    )
                }
            }

        }

    }


}
