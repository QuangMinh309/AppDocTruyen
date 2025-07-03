package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.theme.DeepSpace

@Composable
fun ScreenFrame(
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .imePadding()
            .padding(top = 0.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 0.dp)
        ) {
       //     Spacer(modifier = Modifier.height(20.dp))
            topBar?.invoke()
            content()
            bottomBar?.invoke()
        }
    }
}