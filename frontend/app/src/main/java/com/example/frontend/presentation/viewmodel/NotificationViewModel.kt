package com.example.frontend.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Notification
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.NotificationRepository
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val notificationRepository: NotificationRepository,
) : BaseViewModel(navigationManager) {

    private val _selectionMode = MutableStateFlow(false)

    private val _isLoading = MutableStateFlow(false) // Thêm trạng thái loading cho HotCommunities
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications


    init {
        viewModelScope.launch {
            _isLoading.value = true
            fetchNotifications()
            _isLoading.value = false
        }
    }


    private fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val result = notificationRepository.getNotificationById()
                result.onSuccess { list ->
                    _notifications.value = list
                }.onFailure { fail ->
                    _toast.value = fail.message
                }
            }catch (err:Exception){
                _toast.value = "Can not load notifications !"
                Log.e("From VM Error","Error: ${err.message}")
            }
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            try {
                // Kiểm tra xem thông báo có tồn tại không (tùy chọn)
                if (_notifications.value.none { it.notificationId == id }) {
                    _toast.value = "Notification not found"
                    Log.w("From VM", "Notification with id $id not found in list")
                    return@launch
                }
                val result = notificationRepository.deleteNotification(id)
                result.onSuccess { _notifications.value = _notifications.value.filter { it.notificationId != id }
                    _toast.value = "Notification deleted successfully"
                }.onFailure { fail ->
                    _toast.value = fail.message
                }
            }catch (err:Exception){
                _toast.value = "Can not delete notifications !"
                Log.e("From VM Error","Error: ${err.message}")
            }
        }
    }


}