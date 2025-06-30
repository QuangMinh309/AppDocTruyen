package com.example.frontend.ui.screen.intro_authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.intro_authentification.RegisterViewModel
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.ReemKufifunFontFamily
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//@Preview(showBackground = true)
//@Composable
//fun PreviewScreenContent8() {
//    val fakeViewModel = RegisterViewModel(NavigationManager())
//    RegisterScreen(viewModel = fakeViewModel)
//}

@Suppress("DEPRECATION")
@Composable
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel()) {
    val tbUsernameValue by viewModel.userName.collectAsState()
    val tbConfirmPasswordValue by viewModel.confirmPassword.collectAsState()
    val tbEmailValue by viewModel.email.collectAsState()
    val tbPasswordValue by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userNameError by viewModel.userNameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val dobError by viewModel.dobError.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val toast by viewModel.toast.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val pickedDate by viewModel.dob.collectAsState()
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    // Sử dụng FocusRequester để hỗ trợ di chuyển focus
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DeepSpace,
                        DeepBlue
                    ),
                )
            )
            .paint(
                painterResource(R.drawable.sigup_bg),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
            .imePadding() // Padding khi bàn phím xuất hiện
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp),
                color = OrangeRed
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
                .verticalScroll(state = rememberScrollState()) // Thêm cuộn
                .imePadding(), // Đảm bảo padding khi bàn phím xuất hiện
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Sign up",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        fontSize = 32.sp,
                        lineHeight = 30.sp,
                        color = Color.White,
                    ),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                val annotatedText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    ) {
                        append("If you already have an account, you can ")
                    }

                    pushStringAnnotation(tag = "LOGIN", annotation = "login")
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 17.sp,
                            color = OrangeRed
                        )
                    ) {
                        append("log in here!")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "LOGIN", start = offset, end = offset)
                            .firstOrNull()?.let {
                                viewModel.onGoToLoginScreen()
                            }
                    },
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Email",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = ReemKufifunFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                TextField(
                    value = tbEmailValue,
                    onValueChange = { viewModel.onEmailChange(it) },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your email address", style = TextStyle(color = Color.Gray))
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 53.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.message_1),
                            contentDescription = "message icon",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        focusedLeadingIconColor = BurntCoral,
                        unfocusedLeadingIconColor = BurntCoral,
                        focusedIndicatorColor = BurntCoral,
                        unfocusedIndicatorColor = Color.White
                    ),
                    isError = emailError != null
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Username",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = ReemKufifunFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                TextField(
                    value = tbUsernameValue,
                    onValueChange = { viewModel.onUserNameChange(it) },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your user name", style = TextStyle(color = Color.Gray))
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_bold))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 53.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.user_navigation_ic),
                            contentDescription = "user icon",
                            modifier = Modifier
                                .size(40.dp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        focusedLeadingIconColor = BurntCoral,
                        unfocusedLeadingIconColor = BurntCoral,
                        focusedIndicatorColor = BurntCoral,
                        unfocusedIndicatorColor = Color.White
                    ),
                    isError = userNameError != null
                )
                if (userNameError != null) {
                    Text(
                        text = userNameError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Date of birth",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = ReemKufifunFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = {
                            dateDialogState.show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeRed
                        )
                    ) {
                        Text("Select date", color = DeepSpace, fontFamily = ReemKufifunFontFamily, fontWeight = FontWeight.Normal)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Date picked: ",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = ReemKufifunFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Gray,
                        ),
                    )
                    Text(
                        text = formattedDate,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = ReemKufifunFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.White,
                        ),
                    )
                }
                if (dobError != null) {
                    Text(
                        text = dobError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Password",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                TextField(
                    value = tbPasswordValue,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your password", style = TextStyle(color = Color.Gray))
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.reemkufifun_semibold))),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 53.dp)
                        .focusRequester(passwordFocusRequester), // Gán FocusRequester
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.padlock_1),
                            contentDescription = "padlock icon",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                        )
                    },
                    trailingIcon = {
                        val icon = if (passwordVisible) R.drawable.visible_1 else R.drawable.invisible_1
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = description,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                                .clickable {
                                    passwordVisible = !passwordVisible
                                }
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        focusedLeadingIconColor = BurntCoral,
                        unfocusedLeadingIconColor = BurntCoral,
                        focusedIndicatorColor = BurntCoral,
                        unfocusedIndicatorColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        errorIndicatorColor = Color.Red
                    ),
                    isError = passwordError != null
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Confirm Password",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                TextField(
                    value = tbConfirmPasswordValue,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    singleLine = true,
                    placeholder = {
                        Text("Confirm your password", style = TextStyle(color = Color.Gray))
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.reemkufifun_semibold))),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 53.dp)
                        .focusRequester(confirmPasswordFocusRequester), // Gán FocusRequester
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.padlock_1),
                            contentDescription = "padlock icon",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                        )
                    },
                    trailingIcon = {
                        val icon = if (confirmPasswordVisible) R.drawable.visible_1 else R.drawable.invisible_1
                        val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = description,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                                .clickable {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        focusedLeadingIconColor = BurntCoral,
                        unfocusedLeadingIconColor = BurntCoral,
                        focusedIndicatorColor = BurntCoral,
                        unfocusedIndicatorColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        errorIndicatorColor = Color.Red
                    ),
                    isError = confirmPasswordError != null
                )
                if (confirmPasswordError != null) {
                    Text(
                        text = confirmPasswordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.checkRegister()
                },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(50.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeRed
                )
            ) {
                Text(
                    text = "Let's get started",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Thêm khoảng trống để tránh bị che
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "OK")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick your DOB"
        ) {
            viewModel.onDOBChange(it)
        }
    }
}