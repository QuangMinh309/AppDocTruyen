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
    communityProvider: CommunityProvider
) : BaseViewModel(navigationManager) {
    private val _id = savedStateHandle.getStateFlow("communityId", "")


    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _community = MutableStateFlow<Community?>(null)
    val community: StateFlow<Community?> = _community

    init {
        viewModelScope.launch {
            _id.collect { id ->
                if (id.isNotEmpty()) {
                    try {
                        _isLoading.value = true
                        _community.value = communityProvider.getCommunityById(id.toInt())
                        _isLoading.value = false
                    }
                    catch (err:Exception){
                        _community.value = null
                        Log.e("From VM Error","Error: ${err.message}")
                    }
                }
            }
        }
    }

    fun onGoToChattingScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.Chat.createRoute(id.toString()))
        }
    }
}