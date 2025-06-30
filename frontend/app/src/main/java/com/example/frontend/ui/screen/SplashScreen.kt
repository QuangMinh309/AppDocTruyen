package com.example.frontend.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.R
import com.example.frontend.ui.screen.intro_authentication.IntroScreen
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.FrontendTheme
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontendTheme {
        IntroScreen()
    }
}

@Composable
fun CustomSplashScreen(onFinish: () -> Unit) {
    var progressStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(true) {
        while (progressStep < 5) {
            delay(700)
            progressStep++
        }
        delay(1000)
        onFinish()
    }

    val progressDrawableId = when (progressStep) {
        0 -> R.drawable.loading_0perc
        1 -> R.drawable.loading_20perc
        2 -> R.drawable.loading_40perc
        3 -> R.drawable.loading_60perc
        4 -> R.drawable.loading_80perc
        else -> R.drawable.loading_100perc
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = progressDrawableId),
                contentDescription = "Loading Progress",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
        }
    }
}