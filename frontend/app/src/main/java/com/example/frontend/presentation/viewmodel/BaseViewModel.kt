package com.example.frontend.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.services.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(protected val navigationManager: NavigationManager) : ViewModel() {

    val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> get() = _toast

    fun clearToast() { _toast.value = null }
    fun showToast(message: String) {
        _toast.value = message
    }

    fun formatMoney(money: Long): String {
        val formatter = DecimalFormat("#,###"+"đ")
        return formatter.format(money)
    }


    fun onGoToSetting() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Setting.route)
        }
    }
    fun onGoToNotificationScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Notification.route)
        }
    }
    fun onGoToStoryScreen(storyId: Int) {
        viewModelScope.launch {

            Log.d("StoryId",storyId.toString())
           navigationManager.navigate(Screen.Story.Detail.createRoute(storyId))

        }
    }

    fun onGoToCategoryStoryList(categoryId:Int,categoryName:String){
        Log.d("HomeViewModel", "Navigating to CategoryStoryList: categoryId=$categoryId, categoryName=$categoryName")
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.Category.createRoute(categoryId,categoryName))
        }
    }

    fun onGoToNameListStoryScreen(nameListsId: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.NameLists.createRoute(nameListsId))
        }
    }

    fun onGoBack() {
        viewModelScope.launch {
            navigationManager.back()
        }
    }
    fun onGoToChapterScreen(chapterId: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.Chapter.Read.createRoute(chapterId))
        }
    }
    open fun onGoToSearchingMemberScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Community.SearchingMember.createRoute(id.toString()))
        }
    }
    fun onGoToLoginScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.Login.route)
        }
    }
    fun onGoToRegisterScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.Register.route)
        }
    }
    fun onGoToWriteScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.Chapter.Write.createRoute(id))
        }
    }
    fun onGoToUserProfileScreen(id: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.AuthorProfile.createRoute(id.toString()))
        }
    }
    fun onGoToWithDraw() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Transaction.WithDraw.route)
        }
    }
    fun onGoToDepositScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Transaction.Deposit.route)
        }
    }
    fun onGoToTransactionAcceptScreen(money: Long) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Transaction.Accept.createRoute(money))
        }
    }

    fun onGoToWalletDetailScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Transaction.Wallet.route)
        }
    }
    fun onGoToPremiumScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Transaction.Premium.route)
        }
    }
    fun onGoToResetPassWordScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.ResetPassword.route)
        }
    }
    fun onGoToSetUpPassWordScreen(otp: String, userId: Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.NewPassword.createRoute(otp, userId.toString()))
        }
    }

    fun onGoToTopRankingStoryListScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Story.TopRanking.route)
        }
    }

    fun onGoToAdminScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.AdminScreen.route)
        }
    }

    fun onGoToCategoryMgmtScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Admin.Category.route)
        }
    }

    fun onGoToTransactionMgmtScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Admin.Transaction.route)
        }
    }

    fun onGoToUserMgmtScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Admin.User.route)
        }
    }

    fun onGoToStoryMgmtScreen() {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Admin.Story.route)
        }
    }

    fun onGoToStoryDetailMgmtScreen(id : Int) {
        viewModelScope.launch {
            navigationManager.navigate(Screen.Admin.StoryDetail.createRoute(id.toString()))
        }
    }

    fun onGoToChangePasswordScreen(){
        viewModelScope.launch {
            navigationManager.navigate(Screen.Authentication.ChangePassword.route)
        }
    }
}