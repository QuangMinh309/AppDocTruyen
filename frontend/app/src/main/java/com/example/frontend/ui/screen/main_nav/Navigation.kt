package com.example.frontend.ui.screen.main_nav

import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontend.R
import com.example.frontend.navigation.NavigationCommand
import com.example.frontend.navigation.Screen
import com.example.frontend.presentation.viewmodel.main_nav.AppNavigationViewModel
import com.example.frontend.ui.components.BottomNavigationBar
import com.example.frontend.ui.screen.DiscoverDetailScreen
import com.example.frontend.ui.screen.NotificationScreen
import com.example.frontend.ui.screen.SettingScreen
import com.example.frontend.ui.screen.community.CommunityDetailScreen
import com.example.frontend.ui.screen.community.SearchingMemberScreen
import com.example.frontend.ui.screen.story.ReadScreen
import com.example.frontend.ui.screen.story.StoryDetailScreen
import com.example.frontend.ui.screen.story.UserProfileScreen
import com.example.frontend.ui.screen.story.WriteScreen
import com.example.frontend.ui.screen.story.YourStoryDetailScreen
import com.example.frontend.ui.screen.transaction.DepositScreen
import com.example.frontend.ui.screen.transaction.PremiumScreen
import com.example.frontend.ui.screen.transaction.TransactionAcceptScreen
import com.example.frontend.ui.screen.transaction.WalletDetailScreen
import com.example.frontend.ui.screen.transaction.WithdrawScreen
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
                    Log.e("backk", "Đây là log mức DEBUG")  // Debug
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
        Screen.MainNav.YourStory.route -> true
        "Profile/1" -> true
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
            startDestination = Screen.MainNav.Search.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            //region  main nav
            composable(Screen.MainNav.Home.route) { HomeScreen() }
            composable(Screen.MainNav.YourStory.route) { YourStoryScreen() }
            composable(Screen.MainNav.Search.route) {   StorySearchScreen() }
            composable(Screen.MainNav.Community.route) { CommunityScreen() }
            composable(
                route = Screen.MainNav.Profile.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                )
            ) { ProfileScreen() }
            //endregion

            //region story
            composable(
                route = Screen.Story.Detail.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                )
            ) { StoryDetailScreen() }

            composable(
                route = Screen.Story.Chapter.Read.route,
                arguments = listOf(
                    navArgument("chapterId") { type = NavType.StringType },
                )
            ) { ReadScreen() }

            composable(
                route = Screen.Story.Chapter.Write.route,
                arguments = listOf(
                    navArgument("chapterId") { type = NavType.StringType },
                )
            ) { WriteScreen() }
            //endregion

            //region community
            composable(
                route = Screen.Community.Chat.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType },
                )
            ) { YourStoryDetailScreen() }

            composable(
                route = Screen.Community.Detail.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType },
                )
            ) {
                CommunityDetailScreen()
            }

            composable(
                route = Screen.Community.SearchingMember.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType },
                )
            ) {
                SearchingMemberScreen()
            }
            //endregion

            //region transaction
            composable(Screen.Transaction.Deposit.route) { DepositScreen() }
            composable(Screen.Transaction.Premium.route) { PremiumScreen() }
            composable(Screen.Transaction.Wallet.route) { WalletDetailScreen() }
            composable(Screen.Transaction.WithDraw.route) { WithdrawScreen() }
            composable(Screen.Transaction.Accept.route) { TransactionAcceptScreen() }
            //endregion

            composable(
                route = Screen.YourStoryDetail.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                )
            ) { YourStoryDetailScreen() }

            composable(Screen.Discover.route) { DiscoverDetailScreen() }
            composable(Screen.Notification.route) { NotificationScreen() }
            composable(Screen.Setting.route) { SettingScreen() }
//            composable(
//                route = Screen.StoryList.route,
//                arguments = listOf(
//                    navArgument("id") { type = NavType.StringType },
//                )
//            )  { ReadListScreen() }


            composable(
                route = Screen.Story.UserProfile.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                )
            ) { UserProfileScreen() }

        }
    }
}

