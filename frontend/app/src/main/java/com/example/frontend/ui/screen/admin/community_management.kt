package com.example.frontend.ui.screen.admin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.CommunityMgmtViewModel
import com.example.frontend.ui.components.ScreenFrame

@Composable
fun CommunityManagementScreen(viewModel: CommunityMgmtViewModel = hiltViewModel()) {
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
                    text = "Community",
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
            text = "Create community",
            color = Color.White,
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top = 30.dp)
        )
    }
}