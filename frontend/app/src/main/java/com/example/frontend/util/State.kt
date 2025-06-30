package com.example.frontend.util

sealed class State {
    data object Idle : State()
    data object Success : State()
    data class Error(val message: String) : State()
}