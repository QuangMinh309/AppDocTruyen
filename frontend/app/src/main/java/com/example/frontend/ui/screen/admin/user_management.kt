package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.UserMgmtViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.theme.BurntCoral
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.frontend.ui.components.SelectChip
import com.example.frontend.ui.components.UserCard
import com.example.frontend.ui.theme.DeepBlue

@Composable
fun UserManagementScreen(viewModel : UserMgmtViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val users by viewModel.displayedUsers.collectAsState()
    val selectedUser by viewModel.selectedUser.collectAsState()
    val tbUserNameValue by viewModel.userName.collectAsState()
    val showOnlyLocked by viewModel.showOnlyLocked.collectAsState()
    val toast by viewModel.toast.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    ScreenFrame(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = {viewModel.onGoBack()},
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(25.dp)
                        .weight(0.33f)
                        .wrapContentWidth(Alignment.Start)
                ) {
                    Text(
                        text = "< Back",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                }

                Text(
                    text = "User",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(0.33f))
            }
        }
    )
    {
        Text(
            text = "Find users",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Column ( // find by username
        )
        {
            Text(
                text = "Find by Username",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            TextField(
                value = tbUserNameValue,
                onValueChange = { viewModel.onUserNameChange(it) },
                singleLine = true,
                placeholder = {
                    Text("Enter...", style = TextStyle(color = Color.Gray))
                },
                textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 53.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BurntCoral,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "Show locked users only: ",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                ),
            )
            SelectChip(
                name = if(showOnlyLocked == true) "Yes" else "No",
                isSelected = showOnlyLocked == true,
                onClick = { viewModel.toggleShowOnlyLocked() }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp)
        )
        {
            Button(
                onClick = { viewModel.lockSelectedUser() },
                enabled = selectedUser != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedUser != null) Color.LightGray else Color(0xAFAF2238)
                ),
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = if(selectedUser?.status == "locked") "Unlock" else "Lock",
                    color = if (selectedUser != null) DeepBlue else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }
        }

        FlowColumn (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        )
        {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else{
                users.forEach { user ->
                    UserCard(
                        item = user,
                        isSelected = user == selectedUser,
                        onClick = { viewModel.onSelectedUserChange(user) },
                        onClick2 = { viewModel.onGoToUserProfileScreen(user.id) }
                    )
                }
            }
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewUserScreen()
//{
//    val fakeViewModel = UserMgmtViewModel(NavigationManager())
//    UserManagementScreen(fakeViewModel)
//}