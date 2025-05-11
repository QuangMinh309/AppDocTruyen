package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.frontend.ui.screen.transaction.PremiumScreen
import com.example.frontend.ui.theme.FrontendTheme

class PremiumActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                PremiumScreen()
            }
        }
    }
}