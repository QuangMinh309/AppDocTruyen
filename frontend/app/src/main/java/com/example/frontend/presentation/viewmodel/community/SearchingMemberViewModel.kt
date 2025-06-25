package com.example.frontend.presentation.viewmodel.community

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.CommunityProvider
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingMemberViewModel @Inject constructor(
    navigationManager: NavigationManager,
    savedStateHandle: SavedStateHandle,
    communityProvider: CommunityProvider
) : BaseViewModel(navigationManager) {
    private val _id = savedStateHandle.getStateFlow("communityId", "")

    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _memberList  = MutableStateFlow<List<User>>(emptyList())
    val memberList : StateFlow<List<User>> = _memberList

    init {
        viewModelScope.launch {
            _id.collect { id ->
                if (id.isNotEmpty()) {
                    try {
                        _isLoading.value = true
                        _memberList .value = communityProvider.getCommunityById(id.toInt())?.members?: emptyList()
                        _isLoading.value = false
                    }
                    catch (err:Exception){
                        Log.e("From VM Error","Error: ${err.message}")
                    }
                }
            }
        }
    }


    fun follow(id:Int){
        viewModelScope.launch {

        }
    }
}