package com.example.frontend.presentation.viewmodel.community

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Chat
import com.example.frontend.data.repository.ChatRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager,
    private val chatRepository: ChatRepository
) : BaseViewModel(navigationManager) {
    private val _id = savedStateHandle.getStateFlow("communityId", "")
    val id: StateFlow<String> = _id

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages

    private val _yourChat = MutableStateFlow("")
    val yourChat: StateFlow<String> = _yourChat

    // Cập nhật tin nhắn
    fun updateChat(newText: String) {
        _yourChat.value = newText
    }

    init {

        viewModelScope.launch {
            observeChatState()
            _id.collect { id ->
                if (id.isNotEmpty() && id.toIntOrNull() != null) {
                    try {
                        _isLoading.value = true
                        connect(id.toInt())
                        _isLoading.value = false
                    } catch (err: Exception) {
                        Log.e("ChattingViewModel", "Connection error: ${err.message}")
                        _isLoading.value = false
                    }
                } else {
                    Log.w("ChattingViewModel", "Invalid communityId: $id")
                }
            }
        }
    }

    private fun observeChatState() {
        viewModelScope.launch {
            chatRepository.chatList.collect { chats ->
                _messages.value = chats
                Log.d("ChattingViewModel", "Collected chats: ${chats.size}")
            }
        }
    }

    private fun connect(communityId: Int) {
        viewModelScope.launch {
            try {
                chatRepository.connect(communityId)
                chatRepository.fetchChats(communityId) // Gọi fetch sau khi connect
                Log.d("ChattingViewModel", "Connected and fetched chats for communityId: $communityId")
            } catch (err: Exception) {
                Log.e("ChattingViewModel", "Fetch error: ${err.message}")
            }
        }
    }

    fun createChat(commentPicId: String? = null) {
        viewModelScope.launch {
            try {
                chatRepository.createChat(
                    if (_yourChat.value.isBlank()) null else _yourChat.value.trim(),
                    commentPicId,
                    _id.value.toInt()
                )
                _yourChat.value = "" // Reset sau khi gửi thành công
                Log.d("ChattingViewModel", "Chat created successfully")
            } catch (err: Exception) {
                Log.e("ChattingViewModel", "Create chat error: ${err.message}")
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            chatRepository.disconnect()
            Log.d("ChattingViewModel", "Disconnected")
        }
    }
}