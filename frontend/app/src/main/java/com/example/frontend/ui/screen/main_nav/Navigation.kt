package com.example.frontend.ui.screen.main_nav

//import com.example.frontend.ui.screen.intro_authentication.SetUpPasswordScreen
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.main_nav.AppNavigationViewModel
import com.example.frontend.services.navigation.NavigationCommand
import com.example.frontend.services.navigation.Screen
import com.example.frontend.ui.components.BottomNavigationBar
import com.example.frontend.ui.screen.AddStoryToNameListScreen
import com.example.frontend.ui.screen.CustomSplashScreen
import com.example.frontend.ui.screen.NotificationScreen
import com.example.frontend.ui.screen.SettingScreen
import com.example.frontend.ui.screen.UserReportScreen
import com.example.frontend.ui.screen.admin.AdminScreen
import com.example.frontend.ui.screen.admin.CategoryManagementScreen
import com.example.frontend.ui.screen.admin.CommunityManagementScreen
import com.example.frontend.ui.screen.admin.RevenueManagementScreen
import com.example.frontend.ui.screen.admin.StoryManagementScreen
import com.example.frontend.ui.screen.admin.TransactionManagementScreen
import com.example.frontend.ui.screen.admin.UserManagementScreen
import com.example.frontend.ui.screen.community.ChattingScreen
import com.example.frontend.ui.screen.community.CommunityDetailScreen
import com.example.frontend.ui.screen.community.SearchingMemberScreen
import com.example.frontend.ui.screen.intro_authentication.IntroScreen
import com.example.frontend.ui.screen.intro_authentication.LoginScreen
import com.example.frontend.ui.screen.intro_authentication.RegisterScreen
import com.example.frontend.ui.screen.intro_authentication.ResetPasswordScreen
import com.example.frontend.ui.screen.intro_authentification.ChangePasswordScreen
import com.example.frontend.ui.screen.intro_authentification.SetUpPasswordScreen
import com.example.frontend.ui.screen.story.CategoryStoryListScreen
import com.example.frontend.ui.screen.story.CreateStoryScreen
import com.example.frontend.ui.screen.story.NameListStoryScreen
import com.example.frontend.ui.screen.story.ReadScreen
import com.example.frontend.ui.screen.story.StoryDetailScreen
import com.example.frontend.ui.screen.story.TopRankingStoryListScreen
import com.example.frontend.ui.screen.story.UpdateChapterScreen
import com.example.frontend.ui.screen.story.UpdateStoryScreen
import com.example.frontend.ui.screen.story.WriteScreen

import com.example.frontend.ui.screen.transaction.DepositScreen
import com.example.frontend.ui.screen.transaction.PremiumScreen
import com.example.frontend.ui.screen.transaction.TransactionAcceptScreen
import com.example.frontend.ui.screen.transaction.WalletDetailScreen
import com.example.frontend.ui.screen.transaction.WithdrawScreen
import com.example.frontend.ui.theme.BrightBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.util.UserPreferences

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AppNavigationViewModel = hiltViewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showBottomBar by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var showIntro by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.commands) {

        viewModel.commands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    navController.navigate(command.route) {
                        command.builder.invoke(this)// Áp dụng các tùy chọn từ builder
                    }
                }
                is NavigationCommand.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        UserPreferences.checkLoggedIn(context).collect { loggedIn ->
            if (loggedIn) {
                showIntro = false
            }
        }
    }

    showBottomBar = when (currentRoute) {
        Screen.MainNav.Home.route -> true
        Screen.MainNav.Search.route -> true
        Screen.MainNav.Community.route -> true

        Screen.MainNav.YourStory.route -> true
        Screen.MainNav.Profile.route->true

        "Profile/1" -> true
        else -> false
    }

    Scaffold(
        bottomBar = { if (showBottomBar) BottomNavigationBar(navController) },
        floatingActionButton = {
            if (showBottomBar)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .offset(y = 60.dp)
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
                            containerColor = colorResource(id = R.color.black3),
                            modifier = Modifier.size(58.dp),
                            contentColor = Color.White,
                            shape = CircleShape,
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
            startDestination = Screen.Intro.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.MainNav.Home.route) { HomeScreen() }
            composable(Screen.MainNav.YourStory.route) { YourStoryScreen() }
            composable(Screen.MainNav.Search.route) { StorySearchScreen() }
            composable(Screen.MainNav.Community.route) { CommunityScreen() }
            composable(Screen.MainNav.Profile.route) { ProfileScreen() }

            composable(Screen.Intro.route) { IntroScreen() }

            composable(Screen.Authentication.Login.route) { LoginScreen() }
            composable(Screen.Authentication.Register.route) { RegisterScreen() }
            composable(Screen.Authentication.ResetPassword.route) { ResetPasswordScreen() }
            composable(
                route = Screen.Authentication.NewPassword.route,
                arguments = listOf(
                    navArgument("otp") { type = NavType.StringType },
                    navArgument("userId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val otp = backStackEntry.arguments?.getString("otp") ?: ""
                val userId = backStackEntry.arguments?.getString("userId") ?: "0"
                SetUpPasswordScreen(otp = otp, userId = userId)
            }
            composable(Screen.Authentication.ChangePassword.route) { ChangePasswordScreen()  }

            composable(
                route = Screen.Story.Detail.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.IntType },
                )
            ) { StoryDetailScreen() }

            composable(
                route = Screen.Story.Chapter.Read.route,
                arguments = listOf(
                    navArgument("chapterId") { type = NavType.IntType },
                    navArgument("finalChapterId") {type=NavType.IntType},
                    navArgument("storyId"){type=NavType.IntType},
                    navArgument("isAuthor"){type=NavType.BoolType}
                )
            ) { ReadScreen() }

            composable(
                route = Screen.Story.Chapter.Write.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.IntType },
                )
            ) { WriteScreen() }

            composable(
                route = Screen.Story.Chapter.Update.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.IntType },
                    navArgument("chapterId") {type=NavType.IntType}
                )
            ) { UpdateChapterScreen() }


            composable(
                route = Screen.Story.Category.route,
                arguments = listOf(
                    navArgument("categoryId") {type= NavType.IntType},
                    navArgument("categoryName") {type=NavType.StringType}
                )
            ) {
            CategoryStoryListScreen()
            }

            composable(
                route = Screen.Story.NameLists.route,
                arguments = listOf(
                    navArgument("nameListsId") { type = NavType.IntType }
                )
            ) {
                NameListStoryScreen()
            }

            composable(Screen.Story.TopRanking.route) { TopRankingStoryListScreen()  }
            composable(Screen.Story.CreateStory.route) { CreateStoryScreen()  }
            composable(
                route = Screen.Story.UpdateStory.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.IntType }
                )
            ) {  UpdateStoryScreen() }

            composable(
                route = Screen.Story.AddStoryToNameList.route,
                arguments = listOf(
                    navArgument("storyId") { type = NavType.IntType }
                )
            ){ AddStoryToNameListScreen() }

            composable(
                route = Screen.Community.Chat.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType },
                )
            ) { ChattingScreen() }

            composable(
                route = Screen.Community.Detail.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType }
                   // navArgument("name") { type = NavType.StringType },
                )
            ) { CommunityDetailScreen() }

            composable(
                route = Screen.Community.SearchingMember.route,
                arguments = listOf(
                    navArgument("communityId") { type = NavType.StringType },
                )
            ) { SearchingMemberScreen() }

            composable(Screen.Transaction.Deposit.route) { DepositScreen() }
            composable(Screen.Transaction.Premium.route) { PremiumScreen() }
            composable(Screen.Transaction.Wallet.route) { WalletDetailScreen() }
            composable(Screen.Transaction.WithDraw.route) { WithdrawScreen() }
            composable(
                route = Screen.Transaction.Accept.route,
                arguments = listOf(
                    navArgument("depositMoney") { type = NavType.LongType }
                )
            ) { TransactionAcceptScreen() }

            composable(Screen.Notification.route) { NotificationScreen() }
            composable(Screen.Setting.route) { SettingScreen() }
            composable(
                route = Screen.Report.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("name") { type = NavType.StringType },
                )
            ) { UserReportScreen() }

            composable(
                route = Screen.Story.UserProfile.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                )

            ) { UserProfileScreen() }

            composable(Screen.AdminScreen.route) { AdminScreen() }
            composable(Screen.Admin.Category.route) { CategoryManagementScreen() }
            composable(Screen.Admin.Transaction.route) { TransactionManagementScreen() }
            composable(Screen.Admin.User.route) { UserManagementScreen() }
            composable(Screen.Admin.Story.route) { StoryManagementScreen() }
            composable(Screen.Admin.Community.route) { CommunityManagementScreen() }
            composable(Screen.Admin.Revenue.route) { RevenueManagementScreen() }

        }
    }
}