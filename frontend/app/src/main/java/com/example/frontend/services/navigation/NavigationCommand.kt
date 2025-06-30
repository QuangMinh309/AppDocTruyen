package com.example.frontend.services.navigation

import androidx.navigation.NavOptionsBuilder

sealed class NavigationCommand {
    data class Navigate(val route: String, val builder: NavOptionsBuilder.() -> Unit = {}) : NavigationCommand()
    data object Back : NavigationCommand()
}