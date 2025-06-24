package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Community
import com.example.frontend.data.repository.CategoryProvider
import com.example.frontend.data.repository.CommunityProvider
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val communityProvider: CommunityProvider,
    private val categoryProvider: CategoryProvider
) : BaseViewModel(navigationManager) {
    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _communitiesFollowCategory = MutableStateFlow<List<Community>>(emptyList())
    val communitiesFollowCategory: StateFlow<List<Community>> = _communitiesFollowCategory

    private val _hotCommunities = MutableStateFlow<List<Community>>(emptyList())
    val hotCommunities: StateFlow<List<Community>> = _hotCommunities

    private val _category = MutableStateFlow<List<Category>>(emptyList())
    val category: StateFlow<List<Category>> = _category

    init {
        _isLoading.value=true
        viewModelScope.launch {
            _category.value = categoryProvider.fetchAllCategory()
        }
        fetchCommunitiesFollowCategory(_category.value[0].id)
        fetchHotCommunities()

        _isLoading.value=false
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
            _communitiesFollowCategory.value = communityProvider.filterCommunity(id)
        }
    }

    fun onGoToCommunityDetailScreen(id : Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.Detail.createRoute(id.toString()))
        }
    }
}