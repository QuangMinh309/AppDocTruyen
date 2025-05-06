package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.theme.DeepSpace

@Composable
fun ScreenFrame(
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
){
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
        // Top bar nếu có
        topBar?.invoke()

        // Nội dung chính
        content()

        // Bottom bar nếu có
        bottomBar?.invoke()
    }
}