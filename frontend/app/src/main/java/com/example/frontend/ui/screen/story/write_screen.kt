package com.example.frontend.ui.screen.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.presentation.viewmodel.story.WriteViewModel
import com.example.frontend.ui.components.ScreenFrame

@Preview
@Composable
fun PreviewWriteScreen()
{
    val fakeviewmodel=WriteViewModel(NavigationManager())
    WriteScreen(fakeviewmodel)
}

@Composable
fun WriteScreen(viewModel: WriteViewModel= hiltViewModel()) {
    var content by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    ScreenFrame{

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            //Chapter name
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = "Enter Chapter Name...",
                            color = Color.Gray,
                            fontSize = 20.sp,
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.height(35.dp))
            //content
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
                            text = "Create your story...",
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
                onClick = {},
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .heightIn(39.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Done",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
            }

            Spacer(modifier = Modifier.height(13.dp))
        }
    }
}
