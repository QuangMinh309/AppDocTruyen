package com.example.frontend.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.frontend.R
import com.example.frontend.services.navigation.Screen
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun BottomNavigationBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = colorResource(id = R.color.black1),
        contentColor = colorResource(id = R.color.white),
        tonalElevation = 3.dp,
        modifier = Modifier.height(75.dp)
    ) {
        bottomMenuItemsList.forEachIndexed { index, bottomMenuItem ->
            if (index == 2) {
                // Tạo không gian trống cho FAB
                Spacer(modifier = Modifier.width(60.dp)) // Kích thước FAB là 60dp
                return@forEachIndexed
            }

            val route = when (bottomMenuItem.label) {
                "Home" -> Screen.MainNav.Home.route
                "Search" -> Screen.MainNav.Search.route
                "Community" -> Screen.MainNav.Community.route
                "Profile" -> Screen.MainNav.Profile.route
                else -> Screen.MainNav.Home.route
            }

            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = bottomMenuItem.label,
                        modifier = Modifier.size(70.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangeRed,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource() } // Vô hiệu hóa ripple
            )
        }
    }
}

data class BottomMenuItem(
    val label: String,
    val icon: Painter
)

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem(
            label = "Home",
            icon = painterResource(id = R.drawable.home_navigation_ic)
        ),
        BottomMenuItem(
            label = "Search",
            icon = painterResource(id = R.drawable.search_navigation_ic)
        ),
        BottomMenuItem(
            label = "middle",
            icon = painterResource(id = R.drawable.write_icon)
        ),
        BottomMenuItem(
            label = "Community",
            icon = painterResource(id = R.drawable.community_navigation_ic)
        ),
        BottomMenuItem(
            label = "Profile",
            icon = painterResource(id = R.drawable.user_navigation_ic)
        )
    )
}