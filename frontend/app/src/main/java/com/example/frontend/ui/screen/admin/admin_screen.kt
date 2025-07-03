package com.example.frontend.ui.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.admin.AdminViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.RowSelectItem
import com.example.frontend.ui.components.ScreenFrame

@Composable
fun AdminScreen(viewModel: AdminViewModel = hiltViewModel())
{
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
                    text = "Admin",
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
            text = "Administrative Tools",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            ),
            modifier = Modifier
                .padding(top=30.dp,bottom=10.dp)
        )
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
        )
        {
            RowSelectItem(
                "User",
                painterResource(R.drawable.user_icon),
                { viewModel.onGoToUserMgmtScreen() }
            )
            RowSelectItem(
                "Category",
                painterResource(R.drawable.category_icon),
                { viewModel.onGoToCategoryMgmtScreen() }
            )
            RowSelectItem(
                "Story",
                painterResource(R.drawable.book_icon),
                { viewModel.onGoToStoryMgmtScreen() }
            )
            RowSelectItem(
                "Transaction",
                painterResource(R.drawable.creditcard_icon),
                { viewModel.onGoToTransactionMgmtScreen() }
            )
            RowSelectItem(
                "Community",
                painterResource(R.drawable.people),
                { viewModel.onGoToCommunityMgmtScreen() }
            )
            RowSelectItem(
                "Revenue Report",
                painterResource(R.drawable.file),
                { viewModel.onGoToRevenueMgmtScreen() }
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    val fakeViewModel = AdminViewModel (NavigationManager())
    AdminScreen(fakeViewModel)
}