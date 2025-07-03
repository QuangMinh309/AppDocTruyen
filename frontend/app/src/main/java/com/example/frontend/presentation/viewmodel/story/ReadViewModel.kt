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

import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.CommentRepository
import com.example.frontend.data.repository.ReadRepository
import com.example.frontend.data.repository.StoryDetailRepository
import com.example.frontend.data.repository.UserRepository
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
    private val userRepository: UserRepository,
    private val readRepository: ReadRepository,
    private val storyDetailRepository: StoryDetailRepository,
    private val commentRepository: CommentRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog
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
    private val _selectedBuyChapter = MutableStateFlow<Chapter?>(null)
    val selectedBuyChapter: StateFlow<Chapter?> = _selectedBuyChapter.asStateFlow()
    fun setCommentUri(uri: Uri?) {
        _selectedPicUri.value = uri
    }

    fun purchaseChapter() {
        viewModelScope.launch {
            when (val result = storyDetailRepository.purchaseChapter(_selectedBuyChapter.value?.chapterId?:0)) {
                is Result.Success -> {
                    _toast.value = result.data
                    setShowDialogState(false)
                    _currentChapter.value = selectedBuyChapter.value
                    _chapterId.value = selectedBuyChapter.value?.chapterId?:0 // Cập nhật chapterId hiện tại

                }
                is Result.Failure -> {
                    _toast.value = result.exception.message
                }
            }
        }
    }
    fun setShowDialogState(state :Boolean,selectedChapter:Chapter?=null) {
        _isShowDialog.value = state
        _selectedBuyChapter.value = selectedChapter
    }
    // Cập nhật tin nhắn
    fun updateComment(newText: String) {
        _yourComment.value = newText
    }
    fun back() {
        viewModelScope.launch {
            onCleared()
            onGoBack()
        }
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
    override fun onCleared() {
        super.onCleared()
        disconnect()
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
    fun  changeLikeState(comment:Comment){
        viewModelScope.launch {
            try {

                val result = if (comment.isUserLike) {
                    userRepository.unlike(comment.id)
                } else {
                    userRepository.like(comment.id)
                }
                result.onSuccess {
                    val updatedList = _messages.value.map { com ->
                        if (com.id == comment.id) {
                            com.copy(likeNumber = if (comment.isUserLike) com.likeNumber - 1 else com.likeNumber + 1,isUserLike = !comment.isUserLike)
                        } else {
                            com
                        }
                    }
                    _messages.value=updatedList
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }

            }catch (err:Exception){
                _toast.value = "Can not like/unllike tis comment this user!"
                Log.e("From VM Error","Error: ${err.message}")
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
                    if(result.data.lockedStatus){
                        setShowDialogState(true,result.data)
                    }
                    else{
                        _currentChapter.value = result.data
                        _chapterId.value = result.data.chapterId // Cập nhật chapterId hiện tại
                    }
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