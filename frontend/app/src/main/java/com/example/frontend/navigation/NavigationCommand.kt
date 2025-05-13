package com.example.frontend.navigation

sealed class NavigationCommand {
    data class Navigate(val route: String) : NavigationCommand()
    data object Back : NavigationCommand()
}
