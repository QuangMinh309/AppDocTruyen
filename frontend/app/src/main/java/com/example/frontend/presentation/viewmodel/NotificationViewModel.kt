package com.example.frontend.presentation.viewmodel

import android.util.Log
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
    private val notificationRepository: NotificationRepository
) : BaseViewModel(navigationManager) {

    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode

    private val _isLoading = MutableStateFlow(false)
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

    fun fetchNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = notificationRepository.getNotificationById()
                result.onSuccess { list ->
                    _notifications.value = list
                    Log.d("NotificationViewModel", "Đã tải ${list.size} thông báo")
                }.onFailure { fail ->
                    _toast.value = fail.message
                    Log.e("NotificationViewModel", "Lỗi khi tải thông báo: ${fail.message}")
                }
            } catch (err: Exception) {
                _toast.value = "Không thể tải thông báo!"
                Log.e("NotificationViewModel", "Lỗi: ${err.message}", err)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            try {
                if (_notifications.value.none { it.notificationId == id }) {
                    _toast.value = "Không tìm thấy thông báo"
                    Log.w("NotificationViewModel", "Thông báo với id $id không có trong danh sách")
                    return@launch
                }
                val result = notificationRepository.deleteNotification(id)
                result.onSuccess {
                    _notifications.value = _notifications.value.filter { it.notificationId != id }
                    _toast.value = "Xóa thông báo thành công"
                    Log.d("NotificationViewModel", "Đã xóa thông báo với id $id")
                }.onFailure { fail ->
                    _toast.value = fail.message
                    Log.e("NotificationViewModel", "Lỗi khi xóa thông báo: ${fail.message}")
                }
            } catch (err: Exception) {
                _toast.value = "Không thể xóa thông báo!"
                Log.e("NotificationViewModel", "Lỗi: ${err.message}", err)
            }
        }
    }
}