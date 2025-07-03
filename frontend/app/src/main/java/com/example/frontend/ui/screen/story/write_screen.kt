package com.example.frontend.ui.screen.story

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.story.WriteViewModel
import com.example.frontend.ui.components.ScreenFrame

@Composable
fun WriteScreen(viewModel: WriteViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading
    val toastMessage by viewModel._toast.collectAsState()
    val context = LocalContext.current

    // Hiển thị Toast khi có thông báo
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel._toast.value = null // Xóa toast sau khi hiển thị
        }
    }

    ScreenFrame {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp) // Đảm bảo nút không sát cạnh dưới
        ) {
            // Nội dung chính với thanh cuộn
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 60.dp) // Đệm dưới để nút Done không bị che
            ) {
                Spacer(modifier = Modifier.height(35.dp))
                // Chapter name
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        if (name.isEmpty()) {
                            Text(
                                text = "Enter Chapter Name...",
                                color = Color.Gray,
                                fontSize = 20.sp
                            )
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Content
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 300.dp, max = 650.dp)
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "Create your story...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Nút Done cố định ở dưới cùng
            Button(
                onClick = {
                    if (name.isNotEmpty() && content.isNotEmpty()) {
                        viewModel.createChapter(name, content)
                    } else {
                        viewModel._toast.value = "Please fill in both chapter name and content"
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        text = "Done",
                        color = Color.Black,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                    )
                }
            }
        }
    }
}