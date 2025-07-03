package com.example.frontend.presentation.viewmodel.story

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Chapter
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

    private val _finalChapterId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("finalChapterId")))
    val finalChapterId: StateFlow<Int> = _finalChapterId.asStateFlow()

    // State để lưu chapter hiện tại
    private val _currentChapter = MutableStateFlow<Chapter?>(null)
    val currentChapter: StateFlow<Chapter?> = _currentChapter.asStateFlow()

    // State cho loading
    val isLoading = mutableStateOf(false)

    private val _selectedPicUri = MutableStateFlow<Uri?>(null)
    val selectedPicUri: StateFlow<Uri?> = _selectedPicUri
    
    private val _messages = MutableStateFlow<List<Comment>>(emptyList())
    val messages: StateFlow<List<Comment>> = _messages

    private val _yourComment = MutableStateFlow("")
    val yourComment: StateFlow<String> = _yourComment

    fun setCommentUri(uri: Uri?) {
        _selectedPicUri.value = uri
    }

    // Cập nhật tin nhắn
    fun updateComment(newText: String) {
        _yourComment.value = newText
    }

    init {
        viewModelScope.launch {
            loadChapter()
        }
    }
    init {
        viewModelScope.launch {
        observeCommentState()
            try {
                connect(chapterId.value)
                fetchComments()
            } catch (err: Exception) {
                Log.e("ReadViewModel", "Connection error: ${err.message}")
            }
        }
    }
    private suspend fun fetchComments() {
        commentRepository.isConnected.collect { isConnected ->
            if (isConnected && isLoading.value) {
                commentRepository.fetchComments()
                Log.d("ReadViewModel", "Fetched comments for communityId: ${chapterId.value}")
                isLoading.value = false // Tắt loading sau khi fetch
            }
        }
    }

    private fun observeCommentState() {
        viewModelScope.launch {
            commentRepository.commentList.collect { comments ->
                _messages.value = comments
                Log.d("CommenttingViewModel", "Collected comments: ${comments.size}")
            }
        }
    }

    private suspend fun connect(communityId: Int) {
        commentRepository.connect(communityId)
        Log.d("CommenttingViewModel", "Connected to communityId: $communityId")

    }

    fun createComment(context: Context) {
        viewModelScope.launch {
            try {
                val commentPicData = selectedPicUri.value?.let { uri ->
                    uriToBase64(context, uri)
                }
                commentRepository.createComment(
                    if (_yourComment.value.isBlank()) null else _yourComment.value.trim(),commentPicData)

                _yourComment.value = ""
                _selectedPicUri.value = null // reset sau khi gửi
                Log.d("CommenttingViewModel", "Comment created successfully")
            } catch (err: Exception) {
                Log.e("CommenttingViewModel", "Create comment error: ${err.message}")
            }
        }
    }


    fun disconnect() {
        viewModelScope.launch {
            commentRepository.disconnect()

            Log.d("CommenttingViewModel", "Disconnected")
            onGoBack()
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
                _toast.value = result.exception.message ?: "Failed to load chapter"
                Log.e("ReadViewModel", "Failed to load chapter: ${result.exception.message}")
                if (result.exception.message == "Bạn cần mua chương này để truy cập") {
                    onGoBack()
                }
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
                    _toast.value = result.exception.message ?: "Failed to load next chapter"
                    Log.e("ReadViewModel", "Failed to load next chapter: ${result.exception.message}")
                }
            }
            isLoading.value = false
        }
    }


}