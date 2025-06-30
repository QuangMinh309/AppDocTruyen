package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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

    private val _storyId = MutableStateFlow(checkNotNull(savedStateHandle.get<Int>("storyId")))
    val storyId: StateFlow<Int> = _storyId.asStateFlow()

    val story = mutableStateOf<Story?>(null)
    val isAuthor = mutableStateOf(false)
    val similarStories = mutableStateOf<List<Story>>(emptyList())
    val isLoading = mutableStateOf(false) // Giữ state này cho loading toàn màn hình (nếu cần)

    // State cho loading riêng từng button
    val isLoadingStatus = mutableStateOf(false)
    val isLoadingVote = mutableStateOf(false)

    // State để lưu trạng thái vote
    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted.asStateFlow()

    private val _voteCount = MutableStateFlow(0)
    val voteCount: StateFlow<Int> = _voteCount.asStateFlow()

    // Biến mới để lưu id của chapter đầu tiên
    val firstChapterId = mutableStateOf<Int?>(null)

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
            loadData() // Gọi loadData trong coroutine scope
        }
    }

    private suspend fun loadData() {
        isLoading.value = true
        try {
            loadStoryDetails()
            loadSimilarStories()
            loadVoteStatus() // Đảm bảo tải trạng thái vote khi khởi động
        } finally {
            isLoading.value = false
        }
    }

    suspend fun loadStoryDetails() {
        val result = storyDetailRepository.getStoryById(storyId.value)
        when (result) {
            is Result.Success -> {
                story.value = result.data
                Log.d("StoryDetailViewModel", "Loaded story: ${result.data}")
                Log.d("StoryDetailViewModel", "Chapters: ${result.data.chapters}")
                // Cập nhật firstChapterId nếu có chapters
                firstChapterId.value = result.data.chapters?.minByOrNull { it.ordinalNumber }?.chapterId
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

    // Hàm cập nhật trạng thái với loading riêng
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

    // Hàm loadVoteStatus để kiểm tra trạng thái vote
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
                _hasVoted.value = false // Giá trị mặc định nếu API thất bại
            }
        }
    }

    // Hàm voteStory với loading riêng
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
}