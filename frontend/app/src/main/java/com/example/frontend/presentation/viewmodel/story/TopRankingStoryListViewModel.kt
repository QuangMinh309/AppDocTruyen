package com.example.frontend.presentation.viewmodel.story

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.Story
import com.example.frontend.data.repository.TopRankingRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopRankingStoryListViewModel @Inject constructor(
    private val topRankingRepository: TopRankingRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    private val _topRankingStories = MutableStateFlow<List<Story>>(emptyList())
    val topRankingStories: StateFlow<List<Story>> = _topRankingStories.asStateFlow()

    private val _isTopRankingLoading = MutableStateFlow(false)
    val isTopRankingLoading: StateFlow<Boolean> = _isTopRankingLoading.asStateFlow()

    init {
        loadStories()
    }

    fun loadStories() {
        viewModelScope.launch {
            _isTopRankingLoading.value = true
            try {
                val topRankingResult = topRankingRepository.getStoriesByVote()
                _topRankingStories.value = when (topRankingResult) {
                    is Result.Success -> {
                        topRankingResult.data.forEach { story ->
                            if (story.name == null) Log.w("TopRankingStoryListViewModel", "Truyện với id ${story.id} có tên null")
                        }
                        Log.d("TopRankingStoryListViewModel", "Đã tải ${topRankingResult.data.size} truyện xếp hạng")
                        topRankingResult.data
                    }
                    is Result.Failure -> {
                        Log.e("TopRankingStoryListViewModel", "Lỗi khi tải danh sách truyện xếp hạng: ${topRankingResult.exception.message}", topRankingResult.exception)
                        showToast("Lỗi khi tải danh sách truyện xếp hạng: ${topRankingResult.exception.message}")
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                _topRankingStories.value = emptyList()
                Log.e("TopRankingStoryListViewModel", "Lỗi khi tải danh sách truyện: ${e.message}", e)
                showToast("Lỗi khi tải danh sách truyện: ${e.message}")
            } finally {
                _isTopRankingLoading.value = false
            }
        }
    }
}