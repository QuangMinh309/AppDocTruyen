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
import com.example.frontend.services.navigation.Screen
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
    private val _isLoadingCommunitiesFollowCategory = MutableStateFlow(false) // Thêm trạng thái loading cho CommunitiesFollowCategory
    val isLoadingCommunitiesFollowCategory: StateFlow<Boolean> = _isLoadingCommunitiesFollowCategory

    private val _isLoadingHotCommunities = MutableStateFlow(false) // Thêm trạng thái loading cho HotCommunities
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
           _category.value = categoryProvider.fetchAllCategory()
            _isLoadingCommunitiesFollowCategory.value=true
           fetchCommunitiesFollowCategory(_category.value[0].id)
           _isLoadingCommunitiesFollowCategory.value=false

        }
    }
    init {
        viewModelScope.launch {
            _isLoadingHotCommunities.value = true
            fetchHotCommunities()
            _isLoadingHotCommunities.value = false
        }
    }
    init{

        viewModelScope.launch {
            if(!notificationRepository.isConnected.value)
                notificationRepository.connect()

            try {
                val result = notificationRepository.getUnreadNotificationCount()
                _unReadNotificationsCount.value = when (result) {
                    is Result.Success -> result.data
                    is Result.Failure -> {
                        Log.e("HomeViewModel", "Error loading user", result.exception)
                        0
                    }
                }
            } catch (e: Exception) {
                _unReadNotificationsCount.value =0
                Log.e("HomeViewModel", "Exception during load notificationscount", e)
            } finally {
            }
        }
    }

    private fun fetchHotCommunities() {
        viewModelScope.launch {
            _hotCommunities.value = communityProvider.fetchAllCommunity()
                .sortedByDescending { it.memberNum }
                .take(20)
        }
    }

    fun fetchCommunitiesFollowCategory(id : Int) {
        viewModelScope.launch {
            _isLoadingCommunitiesFollowCategory.value=true
            _communitiesFollowCategory.value = communityProvider.filterCommunity(id)
            _isLoadingCommunitiesFollowCategory.value=false
        }
    }
}