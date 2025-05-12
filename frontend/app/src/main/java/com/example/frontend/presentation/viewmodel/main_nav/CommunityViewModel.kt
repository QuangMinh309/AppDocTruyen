package com.example.frontend.presentation.viewmodel.main_nav

import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Community
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.navigation.Screen
import com.example.frontend.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {

    var hotCommunityList = listOf("Adventure","Fantastic", "Mystery", "Autobiography")
    fun filterCommunityFollowCategory(id : Int):List<Community> { return listOf()  }

    fun onGoToCommunityDetailScreen(id : Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.Detail.createRoute(id.toString()))
        }
    }
}