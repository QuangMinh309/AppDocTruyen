package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.frontend.ui.screen.main_nav.AppNavigation
import com.example.frontend.ui.theme.FrontendTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cho phép Compose xử lý hệ thống insets (bàn phím, thanh điều hướng,...)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FrontendTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

//                            // Thay đổi orientation dựa trên route hiện tại
//                            LaunchedEffect(currentRoute) {
//                                if (currentRoute != null) {
//                                    requestedOrientation = if (currentRoute == Screen.Intro.route || currentRoute.startsWith("Authentication/")) {
//                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                                    } else {
//                                        ActivityInfo.SCREEN_ORIENTATION_SENSOR
//                                    }
//                                }
//                                else
//                                    ActivityInfo.SCREEN_ORIENTATION_SENSOR
//                            }
                    AppNavigation(navController)
                }


            }
        }
    }
}


