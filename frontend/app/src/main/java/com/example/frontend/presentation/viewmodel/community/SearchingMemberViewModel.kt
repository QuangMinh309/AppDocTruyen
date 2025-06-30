package com.example.frontend.presentation.viewmodel.community

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.User
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.CommunityProvider
import com.example.frontend.data.repository.UserRepository
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
    private val communityProvider: CommunityProvider,
    private val userRepository: UserRepository
) : BaseViewModel(navigationManager) {
    private val _id = savedStateHandle.getStateFlow("communityId", "")

    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _memberList  = MutableStateFlow<List<User>>(emptyList())
    val memberList : StateFlow<List<User>> = _memberList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery



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
    fun onSearchQueryChange(query: String) {
     viewModelScope.launch {
         _searchQuery.value = query
          if(!_isLoading.value) _isLoading.value =true
         _memberList.value = communityProvider.searchMembers(_id.value.toInt(),_searchQuery.value)
         if(_isLoading.value) _isLoading.value =false
     }
    }

    fun  changeFollowState(id:Int){
        viewModelScope.launch {
            try {
                if (_memberList.value.any { it.id==id }){
                    val user = _memberList.value.find { it.id==id }
                    if(user == null) return@launch

                    val result = if (user.isFollowed) {
                        userRepository.unFollow(user)
                    } else {
                        userRepository.follow(user)
                    }
                    result.onSuccess {
                         val updatedList = _memberList.value.map { member ->
                             if (member.id == user.id) {
                                 member.copy(isFollowed = !user.isFollowed)
                             } else {
                                member
                            }
                        }
                        _memberList.value=updatedList
                    }.onFailure { error ->
                         Log.e("apiError","Error: ${error.message}")
                    }
                }
                else
                  throw Exception("User not found")
            }catch (err:Exception){
              _toast.value = "Can not follow/unfollow this user!"
              Log.e("From VM Error","Error: ${err.message}")
            }
        }
    }
}