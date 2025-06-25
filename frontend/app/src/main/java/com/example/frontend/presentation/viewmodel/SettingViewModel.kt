package com.example.frontend.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.User
import com.example.frontend.data.repository.AuthRepository
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
    val user = mutableStateOf<User?>(null) // Khởi tạo null, sẽ tải từ API
    val selectedAvatarUri = mutableStateOf<Uri?>(null)
    val selectedBackgroundUri = mutableStateOf<Uri?>(null)
    val displayName = mutableStateOf("")
    val dateOfBirth = mutableStateOf("")
    val username = mutableStateOf("")
    val mail = mutableStateOf<String?>(null)
    val password = mutableStateOf("")
    val showDatePicker = mutableStateOf(false)

    init {
        loadUserData() // Tải dữ liệu người dùng từ API khi khởi tạo
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                val result = authRepository.getUserById(currentUser.id)
                when (result) {
                    is Result.Success -> {
                        user.value = result.data
                        updateFieldsFromUser()
                    }
                    is Result.Failure -> {
                        Log.e("SettingViewModel", "Failed to load user: ${result.exception.message}")
                    }
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
            when (result) {
                is Result.Success -> {
                    user.value = result.data // Cập nhật user với dữ liệu mới từ API
                    updateFieldsFromUser() // Đồng bộ các trường
                    isEditMode.value = false
                    Log.d("SettingViewModel", "Update successful: ${result.data}")
                }
                is Result.Failure -> {
                    Log.e("SettingViewModel", "Update failed: ${result.exception.message}")
                }
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