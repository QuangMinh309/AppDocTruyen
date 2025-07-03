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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible
    val isEditMode = mutableStateOf(false)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    val selectedAvatarUri = mutableStateOf<Uri?>(null)
    val selectedBackgroundUri = mutableStateOf<Uri?>(null)
    val displayName = mutableStateOf("")
    val dateOfBirth = mutableStateOf("")
    val username = mutableStateOf("")
    val mail = mutableStateOf<String?>(null)
    val password = mutableStateOf("")
    val about = mutableStateOf("")
    val showDatePicker = mutableStateOf(false)
    val showDeleteDialog = mutableStateOf(false)

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog

    private val _dialogContent = MutableStateFlow("")
    val dialogContent = _dialogContent

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    if (currentUser.role?.roleName == "admin") {
                        _isVisible.value = true
                    }
                    val result = authRepository.getUserById(currentUser.id)
                    when (result) {
                        is Result.Success -> {
                            _user.value = result.data
                            updateFieldsFromUser()
                            Log.d("SettingViewModel", "User data loaded successfully: ${result.data}")
                        }
                        is Result.Failure -> {
                            Log.e("SettingViewModel", "Failed to load user: ${result.exception.message}")
                            showToast("Failed to load user data: ${result.exception.message}")
                        }
                    }
                } else {
                    Log.e("SettingViewModel", "No current user found")
                    showToast("No current user found")
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Error loading user data: ${e.message}", e)
                showToast("Error loading user data: ${e.message}")
            }
        }
    }

    private fun updateFieldsFromUser() {
        _user.value?.let {
            displayName.value = it.dName ?: ""
            dateOfBirth.value = it.dob ?: ""
            username.value = it.name ?: ""
            mail.value = it.mail
            about.value = it.about ?: ""
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

    private fun saveChanges() {
        viewModelScope.launch {
            try {
                val updateRequest = ApiService.UpdateUserRequest(
                    dUserName = displayName.value.takeIf { it.isNotEmpty() },
                    DOB = dateOfBirth.value.takeIf { it.isNotEmpty() },
                    userName = username.value.takeIf { it.isNotEmpty() },
                    mail = mail.value.takeIf { it?.isNotEmpty() == true },
                    password = password.value.takeIf { it.isNotEmpty() },
                    about = about.value.takeIf { it.isNotEmpty() }
                )
                Log.d("SettingViewModel", "Update request: $updateRequest")

                val avatarFile = selectedAvatarUri.value?.let { authRepository.prepareImageFile(it, "avatarId") }
                val backgroundFile = selectedBackgroundUri.value?.let { authRepository.prepareImageFile(it, "backgroundId") }
                Log.d("SettingViewModel", "Avatar file: $avatarFile, Background file: $backgroundFile")

                val result = authRepository.updateUser(user.value?.id ?: 0, updateRequest, avatarFile, backgroundFile)
                when (result) {
                    is Result.Success -> {
                        _user.value = result.data
                        updateFieldsFromUser()
                        isEditMode.value = false
                        Log.d("SettingViewModel", "Update successful: ${result.data}")
                        showToast("Profile updated successfully")
                    }
                    is Result.Failure -> {
                        Log.e("SettingViewModel", "Update failed: ${result.exception.message}")
                        showToast("Failed to update profile: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Error updating profile: ${e.message}", e)
                showToast("Error updating profile: ${e.message}")
            }
        }
    }

    fun setAvatarUri(uri: Uri?) {
        selectedAvatarUri.value = uri
    }

    fun setBackgroundUri(uri: Uri?) {
        selectedBackgroundUri.value = uri
    }

    fun hideDeleteConfirmation() {
        showDeleteDialog.value = false
    }

    fun showDeleteConfirmation() {
        showDeleteDialog.value = true
    }

    fun deleteUser() {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUser()?.id ?: return@launch
                val result = authRepository.deleteUser()
                when (result) {
                    is Result.Success -> {
                        authRepository.clearToken()
                        onGoToLoginScreen()
                        Log.d("SettingViewModel", "User deleted successfully")
                        showToast("Account deleted successfully")
                    }
                    is Result.Failure -> {
                        Log.e("SettingViewModel", "Failed to delete user: ${result.exception.message}")
                        showToast("Failed to delete user: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Error deleting user: ${e.message}", e)
                showToast("Error deleting user: ${e.message}")
            }
        }
    }

    fun setShowDialogState(isShow: Boolean, content: String = "") {
        _dialogContent.value = content
        _isShowDialog.value = isShow
    }
}