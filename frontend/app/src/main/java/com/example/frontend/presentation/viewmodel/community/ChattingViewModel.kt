package com.example.frontend.presentation.viewmodel.community

import android.content.Context
import android.net.Uri
import android.util.Base64
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
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager,
    private val chatRepository: ChatRepository
) : BaseViewModel(navigationManager) {
    //val communityName = savedStateHandle.getStateFlow("name", "")

    private val _id = savedStateHandle.getStateFlow("communityId", "")
    val id: StateFlow<String> = _id

    private val _selectedPicUri = MutableStateFlow<Uri?>(null)
    val selectedPicUri: StateFlow<Uri?> = _selectedPicUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages

    private val _yourChat = MutableStateFlow("")
    val yourChat: StateFlow<String> = _yourChat

    fun setCommentUri(uri: Uri?) {
        _selectedPicUri.value = uri
    }

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
                        launch {
                            chatRepository.isConnected.collect { isConnected ->
                                if (isConnected && _isLoading.value) {
                                    chatRepository.fetchChats()
                                    Log.d("ChattingViewModel", "Fetched chats for communityId: $id")
                                    _isLoading.value = false // Tắt loading sau khi fetch
                                }
                            }
                        }
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
            chatRepository.connect(communityId)
            Log.d("ChattingViewModel", "Connected to communityId: $communityId")
        }
    }

    fun createChat(context: Context) {
        viewModelScope.launch {
            try {
                val commentPicData = selectedPicUri.value?.let { uri ->
                    uriToBase64(context, uri)
                }
                chatRepository.createChat(
                    if (_yourChat.value.isBlank()) null else _yourChat.value.trim(),commentPicData)

                _yourChat.value = ""
                _selectedPicUri.value = null // reset sau khi gửi
                Log.d("ChattingViewModel", "Chat created successfully")
            } catch (err: Exception) {
                Log.e("ChattingViewModel", "Create chat error: ${err.message}")
            }
        }
    }
    private fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            chatRepository.disconnect()
            Log.d("ChattingViewModel", "Disconnected")
        }
    }
}