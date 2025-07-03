package com.example.frontend.presentation.viewmodel.community

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Community
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
class CommunityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigationManager: NavigationManager,
    private val communityProvider: CommunityProvider
) : BaseViewModel(navigationManager) {
    private val _id = savedStateHandle.getStateFlow("communityId", "")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _community = MutableStateFlow<Community?>(null)
    val community: StateFlow<Community?> = _community

    init {
        viewModelScope.launch {
            _id.collect { id ->
                if (id.isNotEmpty()) {
                    loadCommunity()
                }
            }
        }
    }

    fun loadCommunity() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val id = _id.value.toIntOrNull()
                if (id == null) {
                    _community.value = null
                    Log.e("CommunityDetailViewModel", "ID cộng đồng không hợp lệ: ${_id.value}")
                    showToast("ID cộng đồng không hợp lệ")
                    return@launch
                }
                _community.value = communityProvider.getCommunityById(id)
                Log.d("CommunityDetailViewModel", "Đã tải chi tiết cộng đồng với ID: $id")
            } catch (err: Exception) {
                _community.value = null
                Log.e("CommunityDetailViewModel", "Lỗi khi tải chi tiết cộng đồng: ${err.message}", err)
                showToast("Lỗi khi tải chi tiết cộng đồng: ${err.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onGoToChattingScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.Chat.createRoute(id.toString()))
        }
    }
}