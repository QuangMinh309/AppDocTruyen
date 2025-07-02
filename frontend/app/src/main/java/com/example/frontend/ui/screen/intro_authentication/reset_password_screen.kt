package com.example.frontend.ui.screen.intro_authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.intro_authentification.ResetPasswordViewModel
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.PoppinsFontFamily
import com.example.frontend.ui.theme.ReemKufifunFontFamily

//@Preview(showBackground = true)
//@Composable
//fun PreviewScreenContent7() {
//    val fakeViewModel = ResetPasswordViewModel(NavigationManager())
//    ResetPasswordScreen(viewModel = fakeViewModel)
//}

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = hiltViewModel()) {
    val tbEmailValue by viewModel.email.collectAsState()
    val tbOTPValue by viewModel.otp.collectAsState()
    val toast by viewModel.toast.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(
                Brush.verticalGradient(colors = listOf(DeepSpace, DeepBlue))
            )
            .paint(
                painterResource(R.drawable.sigup_bg),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f)
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Reset your password",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        fontSize = 32.sp,
                        lineHeight = 30.sp,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier.padding(bottom = 50.dp)
                )
                Text(
                    text = "Please enter your email address below for us to send you a recovery code:",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontSize = 17.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.align(alignment = Alignment.Start)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
                TextField(
                    value = tbEmailValue,
                    onValueChange = { viewModel.onEmailChange(it) },
                    singleLine = true,
                    placeholder = { Text("Enter your email address") },
                    textStyle = TextStyle(fontFamily = PoppinsFontFamily),
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
                    )
                )
            }

            Button(
                onClick = { viewModel.sendOTPToEmail() },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(50.dp)
                    .width(240.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeRed)
            ) {
                Text(
                    text = "Send OTP",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "If you've successfully completed the actions above, check your email for the code and fill in the field below:",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 17.sp,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(top = 40.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "OTP",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = ReemKufifunFontFamily,
                        fontSize = 16.sp,
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
                    value = tbOTPValue,
                    onValueChange = { viewModel.onOTPChange(it) },
                    singleLine = true,
                    placeholder = { Text("Enter your code") },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular))),
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
                    )
                )
            }
            Button(
                onClick = { viewModel.checkOTP() },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(50.dp)
                    .width(240.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeRed)
            ) {
                Text(
                    text = "Confirm",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}