package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.ui.screen.CommunityDetailScreen
import com.example.frontend.ui.screen.SearchingMemberScreen

import com.example.frontend.ui.theme.FrontendTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                SearchingMemberScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontendTheme {
        SearchingMemberScreen()
    }
}