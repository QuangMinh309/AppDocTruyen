package com.example.frontend.presentation.viewmodel.main_nav

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Community
import com.example.frontend.data.repository.CategoryProvider
import com.example.frontend.data.repository.CommunityProvider
import com.example.frontend.data.repository.NotificationRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.frontend.data.model.Result

@HiltViewModel
class CommunityViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val communityProvider: CommunityProvider,
    private val notificationRepository: NotificationRepository,
    private val categoryProvider: CategoryProvider
) : BaseViewModel(navigationManager) {
    private val _isLoadingCommunitiesFollowCategory = MutableStateFlow(false)
    val isLoadingCommunitiesFollowCategory: StateFlow<Boolean> = _isLoadingCommunitiesFollowCategory

    private val _isLoadingHotCommunities = MutableStateFlow(false)
    val isLoadingHotCommunities: StateFlow<Boolean> = _isLoadingHotCommunities

    private val _communitiesFollowCategory = MutableStateFlow<List<Community>>(emptyList())
    val communitiesFollowCategory: StateFlow<List<Community>> = _communitiesFollowCategory

    private val _hotCommunities = MutableStateFlow<List<Community>>(emptyList())
    val hotCommunities: StateFlow<List<Community>> = _hotCommunities

    private val _category = MutableStateFlow<List<Category>>(emptyList())
    val category: StateFlow<List<Category>> = _category

    private val _unReadNotificationsCount = MutableStateFlow(0)
    val unReadNotificationsCount: StateFlow<Int> = _unReadNotificationsCount.asStateFlow()

    init {
        viewModelScope.launch {
            loadCategories()
            fetchHotCommunities()
            if (_category.value.isNotEmpty()) {
                fetchCommunitiesFollowCategory(_category.value[0].id)
            }
        }
        viewModelScope.launch {
            if (!notificationRepository.isConnected.value) {
                notificationRepository.connect()
            }
            try {
                val result = notificationRepository.getUnreadNotificationCount()
                _unReadNotificationsCount.value = when (result) {
                    is Result.Success -> result.data
                    is Result.Failure -> {
                        Log.e("CommunityViewModel", "Lỗi khi tải số thông báo chưa đọc", result.exception)
                        0
                    }
                }
            } catch (e: Exception) {
                _unReadNotificationsCount.value = 0
                Log.e("CommunityViewModel", "Lỗi khi tải số thông báo chưa đọc", e)
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoadingCommunitiesFollowCategory.value = true
            try {
                _category.value = categoryProvider.fetchAllCategory()
                Log.d("CommunityViewModel", "Đã tải ${ _category.value.size } danh mục")
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Lỗi khi tải danh mục: ${e.message}", e)
                showToast("Lỗi khi tải danh mục: ${e.message}")
            } finally {
                _isLoadingCommunitiesFollowCategory.value = false
            }
        }
    }

    fun fetchHotCommunities() {
        viewModelScope.launch {
            _isLoadingHotCommunities.value = true
            try {
                _hotCommunities.value = communityProvider.fetchAllCommunity()
                    .sortedByDescending { it.memberNum }
                    .take(20)
                Log.d("CommunityViewModel", "Đã tải ${ _hotCommunities.value.size } cộng đồng hot")
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Lỗi khi tải cộng đồng hot: ${e.message}", e)
                showToast("Lỗi khi tải cộng đồng hot: ${e.message}")
            } finally {
                _isLoadingHotCommunities.value = false
            }
        }
    }

    fun fetchCommunitiesFollowCategory(id: Int) {
        viewModelScope.launch {
            _isLoadingCommunitiesFollowCategory.value = true
            try {
                _communitiesFollowCategory.value = communityProvider.filterCommunity(id)
                Log.d("CommunityViewModel", "Đã tải ${ _communitiesFollowCategory.value.size } cộng đồng theo danh mục $id")
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Lỗi khi tải cộng đồng theo danh mục: ${e.message}", e)
                showToast("Lỗi khi tải cộng đồng theo danh mục: ${e.message}")
            } finally {
                _isLoadingCommunitiesFollowCategory.value = false
            }
        }
    }
}