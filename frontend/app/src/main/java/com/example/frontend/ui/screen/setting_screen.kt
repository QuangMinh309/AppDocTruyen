package com.example.frontend.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.SettingViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    var showImagePicker by remember { mutableStateOf(false) }
    var isAvatarPicker by remember { mutableStateOf(true) }

    // Launcher để chọn ảnh từ gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                if (isAvatarPicker) viewModel.setAvatarUri(uri)
                else viewModel.setBackgroundUri(uri)
            }
            showImagePicker = false
        }
    )

    ScreenFrame(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.onGoBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(25.dp)
                        .weight(0.33f)
                        .wrapContentWidth(Alignment.Start)
                ) {
                    Text("< Back", color = Color.White, style = TextStyle(fontSize = 16.sp))
                }

                Text(
                    text = "Setting",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .align(Alignment.CenterVertically)
                )

                Button(
                    onClick = { viewModel.toggleEditMode() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(25.dp)
                        .weight(0.33f)
                        .wrapContentWidth(Alignment.End)
                ) {
                    Text(
                        text = if (viewModel.isEditMode.value) "Save" else "Edit",
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp)
                .verticalScroll(scrollState)
        ) {
            if (viewModel.user.value == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeRed)
                }
            } else {
                Text(
                    text = "Profile",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
                )

                // Avatar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                        .clickable {
                            if (viewModel.isEditMode.value) {
                                isAvatarPicker = true
                                showImagePicker = true
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = viewModel.selectedAvatarUri.value ?: viewModel.user.value?.avatarUrl ?: R.drawable.intro_page1_bg
                        ),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Avatar",
                            color = Color.White,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                        if (viewModel.isEditMode.value) {
                            Text(
                                text = "Tap to change",
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    thickness = 1.dp,
                    color = Color(0xff202430)
                )

                // Background
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (viewModel.isEditMode.value) {
                                isAvatarPicker = false
                                showImagePicker = true
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = viewModel.selectedBackgroundUri.value ?: viewModel.user.value?.backgroundUrl ?: R.drawable.intro_page1_bg
                        ),
                        contentDescription = "Background",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Background", color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        if (viewModel.isEditMode.value) {
                            Text(
                                text = "Tap to change",
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    thickness = 1.dp,
                    color = Color(0xff202430)
                )

                // Display Name
                EditableField(
                    label = "Display Name",
                    value = viewModel.displayName.value,
                    isEditable = viewModel.isEditMode.value,
                    onValueChange = { viewModel.displayName.value = it }
                )

                // Date of Birth
                EditableField(
                    label = "Date of Birth",
                    value = viewModel.dateOfBirth.value,
                    isEditable = viewModel.isEditMode.value,
                    onValueChange = { viewModel.dateOfBirth.value = it },
                    onClick = { if (viewModel.isEditMode.value) viewModel.showDatePicker() }
                )

                // DatePicker Dialog
                if (viewModel.showDatePicker.value) {
                    val datePickerState = androidx.compose.material3.rememberDatePickerState(
                        initialSelectedDateMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .parse(viewModel.dateOfBirth.value)?.time ?: System.currentTimeMillis()
                    )
                    DatePickerDialog(
                        onDismissRequest = { viewModel.showDatePicker.value = false },
                        confirmButton = {
                            TextButton(onClick = { viewModel.showDatePicker.value = false }) {
                                Text("Cancel")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                                    viewModel.dateOfBirth.value = formattedDate
                                } ?: run {
                                    val calendar = Calendar.getInstance().apply {
                                        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(viewModel.dateOfBirth.value) ?: Date()
                                    }
                                    viewModel.dateOfBirth.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                                }
                                viewModel.showDatePicker.value = false
                            }) {
                                Text("OK")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                // Username
                EditableField(
                    label = "UserName",
                    value = viewModel.username.value,
                    isEditable = viewModel.isEditMode.value,
                    onValueChange = { viewModel.username.value = it }
                )

                // Mail
                EditableField(
                    label = "Mail",
                    value = viewModel.mail.value ?: "",
                    isEditable = viewModel.isEditMode.value,
                    onValueChange = { viewModel.mail.value = it }
                )

                // Password
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onGoToChangePasswordScreen()  },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = if ("Password" == "Display Name") 25.dp else 0.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Password", color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        Text(
                            text = "Change Password",
                            color = Color.White,
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    thickness = 1.dp,
                    color = Color(0xff202430)
                )

                // Wallet (chỉ đọc)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onGoToWalletDetailScreen() },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Wallet", color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                    thickness = 1.dp,
                    color = Color(0xff202430)
                )

                // Premium section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { if(viewModel.user.value?.isPremium==false) viewModel.onGoToPremiumScreen()  }
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = if(viewModel.user.value?.isPremium == true)"Premium" else "Upgrade to Premium",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            brush = Brush.linearGradient(
                                colors =  if(viewModel.user.value?.isPremium == true) listOf(BurntCoral, OrangeRed) else listOf(Color.White, Color.White),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            )
                        )
                    )

                    // Registration Date (sử dụng DOB thay vì createdAt)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 25.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Registration Date", color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                        Text(
                            text = viewModel.user.value?.dob ?: "No Registration Date.",
                            color = Color.White,
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                    // Logout Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onGoToLoginScreen()
                               // viewModel.logout()
                                       },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Logout", color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                            Text(
                                text = "Sign out of your account",
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                        thickness = 1.dp,
                        color = Color(0xff202430)
                    )

                    // Delete Account Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.showDeleteConfirmation()
                                       },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Delete Account", color = Color.Red, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                            Text(
                                text = "Permanently delete your account",
                                color = Color.Red,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }
        }
    }
    // Dialog xác nhận xóa tài khoản
    if (viewModel.showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmation() },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteUser()
                    viewModel.hideDeleteConfirmation()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDeleteConfirmation() }) {
                    Text("No")
                }
            }
        )
    }

    // Mở picker khi showImagePicker = true
    if (showImagePicker) {
        launcher.launch("image/*")
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (label == "Display Name") 25.dp else 0.dp)
            .clickable(enabled = isEditable, onClick = onClick),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(label, color = Color.White, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
        if (isEditable) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) Text(label, color = Color.Gray)
                    innerTextField()
                }
            )
        } else {
            Text(value.ifEmpty { "N/A" }, color = Color.White, style = TextStyle(fontSize = 16.sp))
        }
    }
    if (label != "Wallet") {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
            thickness = 1.dp,
            color = Color(0xff202430)
        )
    }
}