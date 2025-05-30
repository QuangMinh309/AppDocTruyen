package com.example.frontend.ui.screen.main_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import com.example.frontend.navigation.NavigationCommand
import com.example.frontend.navigation.Screen
import com.example.frontend.presentation.viewmodel.AppNavigationViewModel
import com.example.frontend.ui.components.BottomNavigationBar
import com.example.frontend.ui.theme.BrightBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun AppNavigation(viewModel: AppNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showBottomBar by remember { mutableStateOf(true) }

    // Lắng nghe sự kiện điều hướng từ ViewModel
    LaunchedEffect(viewModel.commands) {
        viewModel.commands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    navController.navigate(command.route)
                }
                is NavigationCommand.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }

    //just show bottombar if that screen is in main_nav
    showBottomBar = when (currentRoute) {
        Screen.MainNav.Home.route -> true
        Screen.MainNav.Search.route -> true
        Screen.MainNav.Community.route -> true
        Screen.MainNav.Profile.route -> true
        Screen.MainNav.YourStory.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = {if(showBottomBar) BottomNavigationBar(navController) },
        floatingActionButton ={ if(showBottomBar)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset(y=60.dp)
                    .background(
                        color = DeepSpace.copy(0.7f),
                        shape = CircleShape
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.Center

            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)

                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    OrangeRed,
                                    BrightBlue
                                )
                            ),
                            shape = CircleShape
                        )
                        .padding(3.dp)

                ) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.MainNav.YourStory.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        containerColor = colorResource(id = R.color.black3), // Thay backgroundColor bằng containerColor
                        modifier = Modifier.size(58.dp),
                        contentColor = Color.White,
                        shape = CircleShape, // Thêm shape cho FAB
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.pen_navigation_ic),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp)
                            )
                        },
                    )
                }
            }
        },

        floatingActionButtonPosition = FabPosition.Center,
        containerColor = colorResource(R.color.blackBackground)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainNav.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.MainNav.Home.route) { HomeScreen() }
            composable(Screen.MainNav.YourStory.route) { YourStoryScreen() }
            composable(Screen.MainNav.Search.route) { StoryNavigation() }
            composable(Screen.MainNav.Community.route) { CommunityScreen() }
            composable(Screen.MainNav.Profile.route) {
                AdvancedProfile(
                    backgroundImageUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
                    avatarImageUrl = "",
                    name = "Peneloped Lyne",
                    nickName= "tolapeneloped",
                )
            }
        }
    }
}

