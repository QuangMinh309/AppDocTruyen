package com.example.frontend.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.ReportVIewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.ScreenFrame

@Composable
fun UserReportScreen(viewModel: ReportVIewModel = hiltViewModel())
{
    val context = LocalContext.current
    val name by viewModel.name.collectAsState()
    val toast by viewModel.toast.collectAsState()
    var content by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

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
                    text = "Report",
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // title
            Text(
                text = "Enter report about user  \"$name\"",
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                ),
                modifier = Modifier
                    .padding(top = 15.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Content
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(650.dp)
                    .verticalScroll(rememberScrollState()),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = "Your report here...",
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(19.dp))

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = {
                    if (!content.isBlank()) {
                        showDialog = true
                    } else {
                        Toast.makeText(context,"Please fill in the report field", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .heightIn(39.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Report",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
        }
    }
    if(showDialog)
    {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.reportUser()
                        showDialog = false
                        viewModel.onGoBack()
                    }
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            title = {
                Text("Confirmation of Report", color = Color.White)
            },
            text = {
                Text("Are you sure you want to report this user? We take reports very seriously.", color = Color.LightGray)
            },
            containerColor = Color(0xFF1C1C1C)
        )
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun PreviewReportScreenContent() {
//    val fakeViewModel = ReportVIewModel(NavigationManager())
//    UserReportScreen(viewModel = fakeViewModel)
//}