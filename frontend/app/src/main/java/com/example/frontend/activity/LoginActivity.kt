package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.frontend.ui.screen.LoginScreen
import com.example.frontend.ui.theme.FrontendTheme

class LoginActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                LoginScreen()
            }
        }
    }
}