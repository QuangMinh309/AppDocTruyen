package com.example.frontend.presentation.viewmodel.admin

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.CommunityRequest
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.Result
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.repository.CategoryRepository
import com.example.frontend.data.repository.CommunityProvider
import com.example.frontend.data.repository.CommunityRepository
import com.example.frontend.data.repository.ImageUrlProvider
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CommunityMgmtViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val communityProvider: CommunityProvider,
    private val communityRepository: CommunityRepository,
    private val categoryRepository: CategoryRepository,
    private val imageUrlProvider: ImageUrlProvider
) : BaseViewModel(navigationManager) {
    private val _communities = MutableStateFlow<List<Community>>(emptyList())

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _displayedCommunities = MutableStateFlow<List<Community>>(emptyList())
    val displayedCommunities: StateFlow<List<Community>> = _displayedCommunities

    private val _selectedCommunity = MutableStateFlow<Community?>(null)
    val selectedCommunity: StateFlow<Community?> = _selectedCommunity

    private val _tbSearchValue = MutableStateFlow("")
    val tbSearchValue : MutableStateFlow<String> = _tbSearchValue

    private val _tbCommNameValue = MutableStateFlow("")
    val tbCommNameValue : MutableStateFlow<String> = _tbCommNameValue

    private val _tbCommDescValue = MutableStateFlow("")
    val tbCommDescValue : MutableStateFlow<String> = _tbCommDescValue

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory : MutableStateFlow<Category?> = _selectedCategory

    val selectedAvatarUri = mutableStateOf<Uri?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isShowDialog = MutableStateFlow(false)
    val isShowDialog = _isShowDialog

    init{
        loadCommunities()
        loadCategories()
    }

    fun loadCommunities()
    {
        viewModelScope.launch {
            _isLoading.value = true
            _communities.value = communityProvider.fetchAllCommunity()
            _displayedCommunities.value = _communities.value
            _isLoading.value = false
        }
    }

    fun loadCategories()
    {
        viewModelScope.launch {
            try {
                val result = categoryRepository.getCategories()
                _categories.value = when (result)
                {
                    is Result.Success -> result.data
                    is Result.Failure -> {
                        _toast.value = "Failed to load categories: ${result.exception.message}"
                        emptyList()
                    }
                }
            }
            catch (e: Exception){
                _toast.value = "Error: ${e.message}"
            }
        }
    }

    fun loadDisplayedCommunities()
    {
        _displayedCommunities.value = _communities.value
        if(_tbSearchValue.value != "" && !_tbSearchValue.value.isBlank())
        {
            _displayedCommunities.value = _displayedCommunities.value.filter { it.name.contains(_tbSearchValue.value) }
        }
        if(!_displayedCommunities.value.contains(_selectedCommunity.value)) _selectedCommunity.value = null
    }

    fun createCommunity(context : Context)
    {
        if(_tbCommNameValue.value.isBlank() || _selectedCategory.value == null || selectedAvatarUri.value == null)
            return
        viewModelScope.launch {
            try{
                val file = selectedAvatarUri.value?.let { uriToFile(it, context) }
                val imageInfo = imageUrlProvider.uploadImage(file)
                val community = CommunityRequest(
                    name = _tbCommNameValue.value,
                    description = _tbCommDescValue.value,
                    categoryId = _selectedCategory.value!!.id,
                    memberNum = 0
                )
                val result = communityRepository.createCommunity(community, imageInfo)

                result.onSuccess {
                    loadCommunities()
                    _selectedCommunity.value = it
                    _toast.value = "Community created successfully"
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
            }
            catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("apiError","Error: ${e.message}")
            }
        }
    }

    fun updateCommunity(context : Context)
    {
        if(_selectedCommunity.value == null) return
        if(_tbCommNameValue.value.isBlank() || _selectedCategory.value == null)
            return
        viewModelScope.launch {
            try{
                val file = selectedAvatarUri.value?.let { uriToFile(it, context) }
                val imageInfo = imageUrlProvider.uploadImage(file)
                val community = Community(
                    id = _selectedCommunity.value!!.id,
                    name = _tbCommNameValue.value,
                    description = _tbCommDescValue.value,
                    category = _selectedCategory.value!!,
                    memberNum = _selectedCommunity.value!!.memberNum,
                )
                val result = communityRepository.updateCommunity(community, imageInfo)

                result.onSuccess {
                    loadCommunities()
                    _selectedCommunity.value = null
                    _toast.value = "Community updated successfully"
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
            }
            catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("apiError","Error: ${e.message}")
            }
        }
    }

    fun deleteSelectedCommunity()
    {
        if(_selectedCommunity.value == null) return
        viewModelScope.launch {
            try{
                val result = communityRepository.deleteCommunity(_selectedCommunity.value!!.id)
                result.onSuccess {
                    loadCommunities()
                    _selectedCommunity.value = null
                    _toast.value = "Community deleted successfully"
                }.onFailure { error ->
                    Log.e("apiError","Error: ${error.message}")
                }
            }
            catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
                Log.e("apiError","Error: ${e.message}")
            }
        }
    }

    fun updateFields()
    {
        if(_selectedCommunity.value == null) return
        _tbCommNameValue.value = _selectedCommunity.value!!.name
        _tbCommDescValue.value = _selectedCommunity.value!!.description.toString()
        _selectedCategory.value = _categories.value.find { it.id == _selectedCommunity.value?.category?.id }
    }

    fun clearFields()
    {
        _tbCommNameValue.value = ""
        _tbCommDescValue.value = ""
        _selectedCategory.value = null
        selectedAvatarUri.value = null
    }

    fun onSelectCommunity(community: Community)
    {
        if(_selectedCommunity.value == community) _selectedCommunity.value = null else _selectedCommunity.value = community
    }

    fun onTbSearchValueChange(value: String)
    {
        _tbSearchValue.value = value
        loadDisplayedCommunities()
    }

    fun onTbNameChange(value: String)
    {
        _tbCommNameValue.value = value
    }

    fun onTbDescChange(value: String)
    {
        _tbCommDescValue.value = value
    }

    fun onSelectCategory(category: Category)
    {
        _selectedCategory.value = category
    }

    fun setAvatarUri(uri: Uri?) {
        selectedAvatarUri.value = uri
    }

    fun setShowDialogState(isShow: Boolean) {
        _isShowDialog.value = isShow
    }
}

fun uriToFile(uri: Uri, context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        Log.e("uriToFile", "Failed to convert URI to File: ${e.message}", e)
        null
    }
}