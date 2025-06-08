package com.example.frontend.services.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _commands = MutableSharedFlow<NavigationCommand>()
    val commands: SharedFlow<NavigationCommand> = _commands

    suspend fun navigate(route: String) {
        _commands.emit(NavigationCommand.Navigate(route))
    }

    suspend fun back() {
        _commands.emit(NavigationCommand.Back)
    }
}