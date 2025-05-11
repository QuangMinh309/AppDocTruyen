package com.example.frontend.ui.screen.intro_authentication

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.activity.RegisterActivity
import com.example.frontend.activity.ResetPasswordActivity
import com.example.frontend.data.util.UserPreferences
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import kotlinx.coroutines.launch

@Preview
@Composable
fun LoginScreen()
{
    var tbEmailValue by remember { mutableStateOf("") }
    var tbPasswordValue by remember { mutableStateOf("") }
    var rememberLogin by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        UserPreferences.getUserData(context).collect { (savedEmail, savedPassword, remember) ->
            if (remember) {
                tbEmailValue = savedEmail
                tbPasswordValue = savedPassword
                rememberLogin = true
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DeepSpace,
                        DeepBlue
                    ),
                )
            )
            .paint(
                painterResource(R.drawable.login_bg),
                contentScale = ContentScale.FillWidth   ,
                alignment = Alignment.BottomCenter
            )
    )
    {
        Column( // main body
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Column ( // title and desc
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Login",
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
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle (
                            style = SpanStyle(
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                fontSize = 17.sp,
                                color = Color.White
                            )
                        ) {
                            append("If you don't have an account to login with, you can ")
                        }

                        withStyle (
                            style = SpanStyle(
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                fontSize = 17.sp,
                                color = OrangeRed
                            )
                        ) {
                            append("register here!")
                        }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.Start)
                        .clickable(
                            onClick = {
                                val intent = Intent(context, RegisterActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                )
            }
            Column ( // email textfield
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            {
                Text(
                    text = "Email",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
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
                    onValueChange = { tbEmailValue = it },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your email address")
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_medium))),
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
            Column ( // password textfield
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            {
                Text(
                    text = "Password",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
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
                    onValueChange = { tbPasswordValue = it },
                    singleLine = true,
                    placeholder = {
                        Text("Enter your password")
                    },
                    textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.reemkufifun_semibold))),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 53.dp),
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
                        val icon = if (passwordVisible) R.drawable.invisible_1 else R.drawable.visible_1
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = description,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 20.dp)
                                .clickable{
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
                        unfocusedTrailingIconColor = Color.White
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            { //remember credentials and forgot password redirect
                Row (
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Checkbox(
                        checked = rememberLogin,
                        onCheckedChange = { rememberLogin = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BurntCoral
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Remember me",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght))
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Forgot password?",
                    color = Color.LightGray,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, ResetPasswordActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        if (rememberLogin) {
                            UserPreferences.saveUserData(context, tbEmailValue, tbPasswordValue, true)
                        } else {
                            UserPreferences.clearUserData(context)
                        }
                    }

                },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(50.dp)
                    .fillMaxWidth(0.7f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeRed
                )
            )
            {
                Text(
                    text = "Let's get started",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        color = Color.Black, fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "OR",
                fontSize = 16.sp,
                color = Color(0xD0D0D0D0),
                modifier = Modifier.padding(top = 30.dp)
            )
            Row(
                modifier = Modifier.padding(top = 20.dp)
            )
            {
                IconButton(
                    onClick = {

                    }
                )
                {
                    Image(
                        painter = painterResource(R.drawable.facebook),
                        contentDescription = "facebook",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {

                    }
                )
                {
                    Image(
                        painter = painterResource(R.drawable.xitter),
                        contentDescription = "X (twitter)",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {

                    }
                )
                {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "google",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
//    var intent = Intent(this, HomeActivity::class.java )
//    intent.putExtra("SUBJECTS", i.subjects as Serializable)
//    startActivity(intent)
}