package com.example.frontend.presentation.viewmodel

import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.example.frontend.data.repository.AuthRepository

@HiltViewModel
class SettingViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    private val _isVisible = MutableStateFlow(false)
    val isVisible : StateFlow<Boolean> = _isVisible

    init {
        val user = authRepository.getCurrentUser()
        if(user != null && user.role!!.roleId == 1)
            _isVisible.value = true
    }
}