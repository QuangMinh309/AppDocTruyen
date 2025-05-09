package com.example.frontend.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontend.ui.screen.IntroScreen

import com.example.frontend.ui.theme.FrontendTheme
<<<<<<< HEAD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
=======
import com.example.frontend.AppNavigation

class MainActivity : BaseActivity() {
>>>>>>> origin/navigationscreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
<<<<<<< HEAD
                IntroScreen()

=======
//                ImageUploadScreen()
             AppNavigation()
>>>>>>> origin/navigationscreen
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