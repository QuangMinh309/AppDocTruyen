package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.ui.screen.IntroScreen

import com.example.frontend.ui.theme.FrontendTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                IntroScreen()

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontendTheme {
        IntroScreen()
    }
}