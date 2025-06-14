package com.example.frontend.services.navigation

sealed class NavigationCommand {
    data class Navigate(val route: String) : NavigationCommand()
    data object Back : NavigationCommand()
}
