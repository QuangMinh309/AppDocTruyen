package com.example.frontend.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.AuthRepository
//import com.example.frontend.data.repository.UserRepository
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    navigationManager: NavigationManager
) : BaseViewModel(navigationManager) {

    val isEditMode = mutableStateOf(false)
    val user = mutableStateOf(authRepository.getCurrentUser())
    val selectedAvatarUri = mutableStateOf<Uri?>(null)
    val selectedBackgroundUri = mutableStateOf<Uri?>(null)
    val displayName = mutableStateOf(user.value?.dName ?: "")
    val dateOfBirth = mutableStateOf(user.value?.dob ?: "")
    val username = mutableStateOf(user.value?.name ?: "")
    val mail = mutableStateOf(user.value?.mail)
    val password = mutableStateOf("")
    val showDatePicker = mutableStateOf(false)

    init {
        // Đảm bảo currentUser được tải khi ViewModel khởi tạo (nếu chưa có)
        viewModelScope.launch {
            val currentUserId = user.value?.id ?: 0
            if (currentUserId != 0) {
                val result = authRepository.getUserById(currentUserId)
                if (result is Result.Success) {
                    user.value = result.data
                    updateFieldsFromUser()
                }
            }
        }
    }

    private fun updateFieldsFromUser() {
        user.value?.let {
            displayName.value = it.dName ?: ""
            dateOfBirth.value = it.dob ?: ""
            username.value = it.name ?: ""
            mail.value = it.mail
        }
    }

    fun toggleEditMode() {
        isEditMode.value = !isEditMode.value
        if (!isEditMode.value) saveChanges()
    }

    fun showDatePicker() {
        showDatePicker.value = true
    }

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        dateOfBirth.value = formattedDate
        showDatePicker.value = false
    }

    fun saveChanges() {
        viewModelScope.launch {
            val updateRequest = ApiService.UpdateUserRequest(
                dUserName = displayName.value.takeIf { it.isNotEmpty() },
                DOB = dateOfBirth.value.takeIf { it.isNotEmpty() },
                userName = username.value.takeIf { it.isNotEmpty() },
                mail = mail.value.takeIf { it?.isNotEmpty() == true },
                password = password.value.takeIf { it.isNotEmpty() }
            )
            Log.d("SettingViewModel", "Update request: $updateRequest")

            val avatarFile = selectedAvatarUri.value?.let { authRepository.prepareImageFile(it, "avatarId") }
            val backgroundFile = selectedBackgroundUri.value?.let { authRepository.prepareImageFile(it, "backgroundId") }
            Log.d("SettingViewModel", "Avatar file: $avatarFile, Background file: $backgroundFile")

            val result = authRepository.updateUser(user.value?.id ?: 0, updateRequest, avatarFile, backgroundFile)
            if (result is Result.Success) {
                user.value = result.data
                updateFieldsFromUser() // Cập nhật lại các trường
                isEditMode.value = false
                Log.d("SettingViewModel", "Update successful: ${result.data}")
            } else if (result is Result.Failure) {
                Log.e("SettingViewModel", "Update failed: ${result.exception.message}")
            }
        }
    }

    fun setAvatarUri(uri: Uri?) {
        selectedAvatarUri.value = uri
    }

    fun setBackgroundUri(uri: Uri?) {
        selectedBackgroundUri.value = uri
    }
}