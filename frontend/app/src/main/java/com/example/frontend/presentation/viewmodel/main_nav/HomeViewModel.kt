package com.example.frontend.presentation.viewmodel.main_nav

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Story
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    // Danh sách truyện gợi ý
    private val _suggestedStories = mutableStateOf<List<Story>>(emptyList())
    val suggestedStories: List<Story> get() = _suggestedStories.value

    // Danh sách truyện mới
    private val _newStories = mutableStateOf<List<Story>>(emptyList())
    val newStories: List<Story> get() = _newStories.value

    // Danh sách truyện top ranking
    private val _topRankingStories = mutableStateOf<List<Story>>(emptyList())
    val topRankingStories: List<Story> get() = _topRankingStories.value

    // Trạng thái loading
    private val _isStoriesLoading = mutableStateOf(false)
    val isStoriesLoading: Boolean get() = _isStoriesLoading.value

    init {
        loadStories()
    }

    private fun loadStories() {
        viewModelScope.launch {
            _isStoriesLoading.value = true
            try {
                val response = apiService.getStories()
                if (response.isSuccessful) {
                    val stories = response.body() ?: emptyList()

                    // Gợi ý: Lấy tất cả truyện
                    _suggestedStories.value = stories

                    // Truyện mới: Sắp xếp theo createdAt (giả sử có trường createdAt)
                    _newStories.value = stories.sortedByDescending { it.createdAt ?: LocalDate.now() }

                    // Top ranking: Sắp xếp theo voteNum (giả sử có trường voteNum)
                    _topRankingStories.value = stories.sortedByDescending { it.voteNum ?: 0 }
                } else {
                    // Xử lý lỗi nếu cần
                    _suggestedStories.value = emptyList()
                    _newStories.value = emptyList()
                    _topRankingStories.value = emptyList()
                }
            } catch (e: Exception) {
                _suggestedStories.value = emptyList()
                _newStories.value = emptyList()
                _topRankingStories.value = emptyList()
            } finally {
                _isStoriesLoading.value = false
            }
        }
    }

//    fun onGoToStoryScreen(storyId: Int) {
//        // Điều hướng đến màn hình chi tiết truyện
//        navigate("story/$storyId")
//    }
//
//    fun onGoToNotificationScreen() {
//        navigate("notifications")
//    }
//
//    fun onGoToSetting() {
//        navigate("settings")
//    }
}