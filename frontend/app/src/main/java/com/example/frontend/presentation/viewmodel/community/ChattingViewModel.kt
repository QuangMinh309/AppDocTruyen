package com.example.frontend.presentation.viewmodel.community

import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    val communityId = 1
    private val _yourChat = MutableStateFlow("")
    val yourChat: StateFlow<String> = _yourChat

    // Cập nhật tin nhắn
    fun updateChat(newText: String) {
        _yourChat.value = newText
    }

}