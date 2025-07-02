package com.example.frontend.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.UserProfileRepository
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportUserViewModel  @Inject constructor(
   savedStateHandle: SavedStateHandle,
   private val userProfileRepository: UserProfileRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {
    private val _content = MutableStateFlow("")
    val content: MutableStateFlow<String> = _content

    private val _name = MutableStateFlow(checkNotNull(savedStateHandle.get<String>("name")))
    val name: MutableStateFlow<String> = _name

    private val _userId = MutableStateFlow<Int?>(checkNotNull(savedStateHandle.get<Int>("userId")))
    val userId: MutableStateFlow<Int?> = _userId

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog

    fun reportUser()
    {
        if(_userId.value == null || _content.value.isEmpty())
        {
            _toast.value = "Please fill in all fields"
            return
        }
        viewModelScope.launch {
            try {
                val result = userProfileRepository.reportUser(_userId.value!!, _content.value)
                result.onSuccess { message ->
                    _toast.value = message
                }.onFailure { error ->
                    _toast.value = "Error: ${error.message}"
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
            }
        }
    }

    fun onContentChange(content: String) {
        _content.value = content
    }

    fun setShowDialogState(state: Boolean) {
        _isShowDialog.value = state
    }
}