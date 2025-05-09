package com.example.frontend

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = colorResource(id = R.color.black3),
        contentColor = colorResource(id=R.color.white),
        elevation = 3.dp
    ) {
        bottomMenuItemsList.forEachIndexed { index, bottomMenuItem ->
            if (index == 2) {
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {},
                    enabled = false
                )
            }

            val route = when(bottomMenuItem.label) {
                "Home" -> Screen.Home.route
                "Search" -> Screen.Search.route
                "Community" -> Screen.Community.route
                "User" -> Screen.User.route
                else -> Screen.Home.route
            }

            BottomNavigationItem(
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
                        modifier = Modifier.size(50.dp)
                    )
                },
//                    label = {
//                        Text(
//                            text = bottomMenuItem.label,
//                            modifier = Modifier.padding(top = 14.dp)
//                        )
//                    },
                alwaysShowLabel = true
            )

        }
    }
}

data class BottomMenuItem(
    val label:String,val icon: Painter
)

@Composable
fun prepareBottomMenu():List<BottomMenuItem>{
    return listOf(
        BottomMenuItem(
            label = "Home",
            icon = painterResource(id=R.drawable.home_navigation_ic)
        ),
        BottomMenuItem(
            label = "Search",
            icon = painterResource(id=R.drawable.search_navigation_ic)
        ),
        BottomMenuItem(
            label = "Community",
            icon = painterResource(id=R.drawable.community_navigation_ic)
        ),
        BottomMenuItem(
            label = "User",
            icon = painterResource(id=R.drawable.user_navigation_ic)
        )
    )
}