package com.example.frontend.presentation.viewmodel.admin

import com.example.frontend.data.api.User
import com.example.frontend.data.model.Role
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

val dummyUsers = listOf(
    User(
        1,
        "asdgooiwre",
        1,
        "@lmao",
        "sall@gmail.com",
        null,
        0,
        null,
        null,
        "0000000000",
        false,
        "Free",
        Role(
            1,
            "user"
        ),
        "2005-09-11"
    ),
    User(
        2,
        "asdgdsage",
        1,
        "@lmaoxd",
        "sall@gmail.com",
        null,
        0,
        null,
        null,
        "0000000000",
        false,
        "Free",
        Role(
            1,
            "user"
        ),
        "2005-09-11"
    ),
    User(
        3,
        "assaddsfdgooiwre",
        1,
        "@lmaoxdd",
        "sall@gmail.com",
        null,
        0,
        null,
        null,
        "0000000000",
        false,
        "Free",
        Role(
            1,
            "user"
        ),
        "2005-09-11"
    ),
    User(
        6,
        "asdgooiwre",
        1,
        "@lmaoxddfdfd",
        "sall@gmail.com",
        null,
        0,
        null,
        null,
        "0000000000",
        false,
        "Suspended",
        Role(
            1,
            "user"
        ),
        "2005-09-11"
    ),
    User(
        12,
        "lol",
        1,
        "@ewfgvreg",
        "sall@gmail.com",
        null,
        0,
        null,
        null,
        "0000000000",
        false,
        "Suspended",
        Role(
            1,
            "user"
        ),
        "2005-09-11"
    ),
)

@HiltViewModel
class UserMgmtViewModel @Inject constructor(navigationManager: NavigationManager) : BaseViewModel(navigationManager) {
    private val _users = MutableStateFlow<List<User>>(dummyUsers)
    val users : StateFlow<List<User>> = _users

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser : StateFlow<User?> = _selectedUser

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _showOnlySuspended = MutableStateFlow(false)
    val showOnlySuspended: StateFlow<Boolean> = _showOnlySuspended

    fun onUserNameChange(name : String)
    {
        _userName.value = name
    }

    fun toggleShowOnlySuspended()
    {
        _showOnlySuspended.value = !_showOnlySuspended.value
    }

    fun onSelectedUserChange(user : User)
    {
        _selectedUser.value = if(_selectedUser.value == user) null else user
    }
}