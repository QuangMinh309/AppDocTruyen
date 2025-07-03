
package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Chapter
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.AuthRepository
import com.example.frontend.data.repository.StoryDetailRepository
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storyDetailRepository: StoryDetailRepository,
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    private val _selectedBuyChapter = MutableStateFlow<Chapter?>(null)
    val selectedBuyChapter: StateFlow<Chapter?> = _selectedBuyChapter.asStateFlow()

    val story = mutableStateOf<Story?>(null)
    val isAuthor = mutableStateOf(false)
    val similarStories = mutableStateOf<List<Story>>(emptyList())
    val isLoading = mutableStateOf(false)
    val isLoadingStatus = mutableStateOf(false)
    val isLoadingVote = mutableStateOf(false)

    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted.asStateFlow()

    private val _voteCount = MutableStateFlow(0)
    val voteCount: StateFlow<Int> = _voteCount.asStateFlow()

    val firstChapterId = mutableStateOf<Int?>(null)
    val finalChapterId= mutableStateOf<Int?>(null)

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("storyId", 0).collect { newStoryId ->
                if (_storyId.value != newStoryId) {
                    _storyId.value = newStoryId
                    loadData()
                }
            }
        }
        viewModelScope.launch {
            loadData()
        }
    }


    fun setShowDialogState(state :Boolean,selectedChapter:Chapter?=null) {
        _isShowDialog.value = state
        _selectedBuyChapter.value = selectedChapter
    }
    private suspend fun loadData() {
        isLoading.value = true
        try {
            loadStoryDetails()
            loadSimilarStories()
            loadVoteStatus()
        } finally {
            isLoading.value = false
        }
    }
    fun purchaseChapter() {
        viewModelScope.launch {
            when (val result = storyDetailRepository.purchaseChapter(_selectedBuyChapter.value?.chapterId?:0)) {
                is Result.Success -> {
                     _toast.value = result.data
                    setShowDialogState(false)
                        onGoToChapterScreen( _selectedBuyChapter.value?.chapterId?:0,finalChapterId.value?:0,
                            _selectedBuyChapter.value?.storyId?:0,
                            isAuthor.value)

                }
                is Result.Failure -> {
                    _toast.value = result.exception.message
                }
            }
        }
    }
    suspend fun loadStoryDetails() {
        val result = storyDetailRepository.getStoryById(storyId.value)
        when (result) {
            is Result.Success -> {
                story.value = result.data
                Log.d("StoryDetailViewModel", "Loaded story: ${result.data}")
                Log.d("StoryDetailViewModel", "Chapters: ${result.data.chapters}")
                firstChapterId.value = result.data.chapters?.minByOrNull { it.ordinalNumber }?.chapterId
                finalChapterId.value=result.data.chapters?.maxByOrNull { it.ordinalNumber }?.chapterId
                checkIsAuthor()
            }
            is Result.Failure -> {
                _toast.value = "Failed to load story: ${result.exception.message}"
            }
        }
    }

    fun loadSimilarStories() {
        viewModelScope.launch {
            val result = storyDetailRepository.getAllStories()
            when (result) {
                is Result.Success -> {
                    similarStories.value = result.data
                }
                is Result.Failure -> {
                    _toast.value = "Failed to load similar stories: ${result.exception.message}"
                }
            }
        }
    }

    private fun checkIsAuthor() {
        val currentUser = authRepository.getCurrentUser()
        story.value?.author?.id?.let { authorId ->
            isAuthor.value = currentUser?.id == authorId
        }
    }

    fun updateStoryStatus() {
        viewModelScope.launch {
            Log.d("StoryDetailViewModel", "Starting updateStoryStatus for storyId: ${storyId.value}")
            isLoadingStatus.value = true
            val currentStory = story.value ?: run {
                Log.e("StoryDetailViewModel", "No story data available")
                isLoadingStatus.value = false
                return@launch
            }
            val newStatus = if (currentStory.status == "update") "full" else "update"
            Log.d("StoryDetailViewModel", "Sending request to update status to: $newStatus")
            val result = storyDetailRepository.updateStory(storyId.value, newStatus)
            when (result) {
                is Result.Success -> {
                    story.value = result.data
                    Log.d("StoryDetailViewModel", "Updated story status to: ${result.data?.status}")
                    _toast.value = "Cập nhật trạng thái thành công"
                }
                is Result.Failure -> {
                    Log.e("StoryDetailViewModel", "Failed to update story status: ${result.exception.message}", result.exception)
                    _toast.value = "Failed to update story status: ${result.exception.message}"
                    story.value?.let { story.value = it.copy(status = currentStory.status) }
                }
            }
            isLoadingStatus.value = false
            Log.d("StoryDetailViewModel", "Finished updateStoryStatus")
        }
    }

    suspend fun loadVoteStatus() {
        Log.d("StoryDetailViewModel", "Checking vote status for storyId: ${storyId.value}")
        val result = storyDetailRepository.checkVote(storyId.value)
        when (result) {
            is Result.Success -> {
                _hasVoted.value = result.data.hasVoted
                _voteCount.value = result.data.voteCount
                Log.d("StoryDetailViewModel", "Vote status loaded - hasVoted: ${result.data.hasVoted}, voteCount: ${result.data.voteCount}")
            }
            is Result.Failure -> {
                Log.e("StoryDetailViewModel", "Failed to check vote status: ${result.exception.message}", result.exception)
                _toast.value = "Failed to check vote status: ${result.exception.message}"
                _hasVoted.value = false
            }
        }
    }

    fun voteStory() {
        viewModelScope.launch {
            Log.d("StoryDetailViewModel", "Voting for storyId: ${storyId.value}")
            isLoadingVote.value = true
            val result = storyDetailRepository.voteStory(storyId.value)
            when (result) {
                is Result.Success -> {
                    _hasVoted.value = result.data.data.hasVoted
                    _voteCount.value = result.data.data.voteCount
                    Log.d("StoryDetailViewModel", "Vote result - hasVoted: ${result.data.data.hasVoted}, voteCount: ${result.data.data.voteCount}, message: ${result.data.data.message}")
                    _toast.value = result.data.data.message
                }
                is Result.Failure -> {
                    Log.e("StoryDetailViewModel", "Failed to vote: ${result.exception.message}", result.exception)
                    _toast.value = "Failed to vote: ${result.exception.message}"
                }
            }
            isLoadingVote.value = false
            Log.d("StoryDetailViewModel", "Finished voting")
        }
    }

    suspend fun deleteChapters(chapterIds: List<Int>) {
        Log.d("StoryDetailViewModel", "Deleting chapters: $chapterIds")
        try {
            chapterIds.forEach { chapterId ->
                val result = storyDetailRepository.deleteChapter(chapterId)
                when (result) {
                    is Result.Success -> {
                        Log.d("StoryDetailViewModel", "Deleted chapter $chapterId: ${result.data.message}")
                        _toast.value = result.data.message
                    }
                    is Result.Failure -> {
                        Log.e("StoryDetailViewModel", "Failed to delete chapter $chapterId: ${result.exception.message}")
                        _toast.value = "Failed to delete chapter $chapterId: ${result.exception.message}"
                        throw result.exception
                    }
                }
            }
            // Làm mới dữ liệu sau khi xóa
            loadStoryDetails()
        } catch (e: Exception) {
            Log.e("StoryDetailViewModel", "Error deleting chapters: ${e.message}")
            _toast.value = "Lỗi khi xóa chương: ${e.message}"
        }
    }
}
