package com.example.frontend.services.navigation

import android.util.Log
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _commands = MutableSharedFlow<NavigationCommand>()
    val commands: SharedFlow<NavigationCommand> = _commands

    suspend fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
        Log.d("NavigationManager", "Navigating to: $route")
        val navCommand = NavigationCommand.Navigate(route, builder)
        _commands.emit(navCommand)
    }

    suspend fun back() {
        _commands.emit(NavigationCommand.Back)
    }
}