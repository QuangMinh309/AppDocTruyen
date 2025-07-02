package com.example.frontend.presentation.viewmodel.story

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Chat
import com.example.frontend.data.model.Comment
import com.example.frontend.data.repository.CommentRepository
import com.example.frontend.data.repository.ReadRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readRepository: ReadRepository,
    private val commentRepository: CommentRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _chapterId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("chapterId")))
    val chapterId: StateFlow<Int> = _chapterId.asStateFlow()

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    private val _isAuthor = MutableStateFlow(checkNotNull(savedStateHandle.get<Boolean>("isAuthor")))
    val isAuthor: StateFlow<Boolean> = _isAuthor.asStateFlow()

    // State để lưu chapter hiện tại
    private val _currentChapter = MutableStateFlow<Chapter?>(null)
    val currentChapter: StateFlow<Chapter?> = _currentChapter.asStateFlow()

    // State cho loading
    val isLoading = mutableStateOf(false)

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _yourChat = MutableStateFlow("")
    val yourChat: StateFlow<String> = _yourChat

    private val _selectedPicUri = MutableStateFlow<Uri?>(null)
    val selectedPicUri: StateFlow<Uri?> = _selectedPicUri


    init {
        viewModelScope.launch {
            observeCState()
            loadChapter()
            try {
                isLoading.value = true
                connect(_chapterId.value)
                launch {
                    commentRepository.isConnected.collect { isConnected ->
                        if (isConnected && isLoading.value) {
                            commentRepository.fetchComments()
                            Log.d("ReadViewModel", "Fetched comment for chapterId: ${_chapterId.value}")
                            isLoading.value = false // Tắt loading sau khi fetch
                        }
                    }
                }
            } catch (err: Exception) {
                Log.e("ReadViewModel", "Connection error: ${err.message}")
                isLoading.value = false
            }
        }
    }

    private suspend fun loadChapter() {
        isLoading.value = true
        val result = readRepository.getChapter(chapterId.value)
        when (result) {
            is Result.Success -> {
                _currentChapter.value = result.data
                Log.d("ReadViewModel", "Loaded chapter: ${result.data.chapterName}")
            }
            is Result.Failure -> {
                _toast.value = "Failed to load chapter: ${result.exception.message}"
            }
        }
        isLoading.value = false
    }

    fun goToNextChapter() {
        viewModelScope.launch {
            isLoading.value = true
            val result = readRepository.getNextChapter(chapterId.value)
            when (result) {
                is Result.Success -> {
                    _currentChapter.value = result.data
                    _chapterId.value = result.data.chapterId // Cập nhật chapterId hiện tại
                    Log.d("ReadViewModel", "Loaded next chapter: ${result.data.chapterName}")
                }
                is Result.Failure -> {
                    _toast.value = "Failed to load next chapter: ${result.exception.message}"
                }
            }
            isLoading.value = false
        }
    }

    init {
        viewModelScope.launch {
            observeCState()
            _chapterId.collect { id ->

                try {
                    isLoading.value = true
                    connect(id)
                    launch {
                        commentRepository.isConnected.collect { isConnected ->
                            if (isConnected && isLoading.value) {
                                commentRepository.fetchComments()
                                Log.d("ReadViewModel", "Fetched comment for chapterId: $id")
                                isLoading.value = false // Tắt loading sau khi fetch
                            }
                        }
                    }
                } catch (err: Exception) {
                    Log.e("ReadViewModel", "Connection error: ${err.message}")
                    isLoading.value = false
                }

            }
        }
    }

    fun setCommentUri(uri: Uri?) {
        _selectedPicUri.value = uri
    }


    private suspend  fun observeCState() {
    commentRepository.commentList.collect { chats ->
            _comments.value = chats
            Log.d("ChattingViewModel", "Collected chats: ${chats.size}")
        }

    }

    private suspend fun connect(communityId: Int) {

        commentRepository.connect(communityId)
            Log.d("ChattingViewModel", "Connected to communityId: $communityId")

    }

    suspend fun  createComment(context: Context) {
            try {
                val commentPicData = selectedPicUri.value?.let { uri ->
                    uriToBase64(context, uri)
                }
                commentRepository.createComment(
                    if (_yourChat.value.isBlank()) null else _yourChat.value.trim(),commentPicData)

                _yourChat.value = ""
                _selectedPicUri.value = null // reset sau khi gửi
                Log.d("ChattingViewModel", "Chat created successfully")
            } catch (err: Exception) {
                Log.e("ChattingViewModel", "Create chat error: ${err.message}")
            }

    }


    fun disconnect() {
        viewModelScope.launch {
            commentRepository.disconnect()
            Log.d("ChattingViewModel", "Disconnected")
        }
    }
}