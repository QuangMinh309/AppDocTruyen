package com.example.frontend.ui.screen.transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.transaction.DepositViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.BrightAquamarine
import java.text.DecimalFormat
import androidx.compose.foundation.layout.Box as Box1

@Composable
fun DepositScreen(viewModel: DepositViewModel = hiltViewModel()) {
    // State để lưu giá trị số tiền dưới dạng Long
    val amountState = viewModel.amountState.collectAsState()
    val amountTextState = rememberSaveable() { mutableStateOf("") }
    val selectedTag by viewModel.selectedTag.collectAsState()

    val toast by viewModel.toast.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    LaunchedEffect(amountState.value) {
        amountTextState.value = viewModel.formatMoney(amountState.value)
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Deposit money",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()

        ) {
            // Deposit info
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 66.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Enter the amount you want to deposit",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                // Money enter text box
                BasicTextField(
                    value = amountTextState.value,
                    onValueChange = { newValue ->
                        // Làm sạch chuỗi nhập
                        val cleaned = newValue.replace("[^\\d]".toRegex(), "")
                        val newAmount = cleaned.toLongOrNull() ?: 0L

                        // Cập nhật Long và hiển thị format
                        viewModel.changeAmount(newAmount)
                        amountTextState.value = viewModel.formatMoney(newAmount)
                    },
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .background(Color(0xA6747373), shape = RoundedCornerShape(5.dp))
                        .padding(vertical = 15.dp, horizontal = 8.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            innerTextField()
                        }
                    }
                )

                // Suggested amount
                val amountButtons = listOf(50000L,200000L,500000L)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.End)
                ) {
                    amountButtons.forEachIndexed { index, amount ->
                        Box1(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(35.dp)
                                .weight(0.3f)
                                .background(Color.Transparent, RoundedCornerShape(5.dp))
                                .border(1.dp,if(selectedTag == index ) BrightAquamarine else Color.Gray, RoundedCornerShape(5.dp))
                                .clickable {
                                    viewModel.changeAmount(amount)
                                    viewModel.changeSelectedTag(index)

                                }
                        ) {
                            Text(
                                text = viewModel.formatMoney(amount),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }

                Text(
                    text = "Please enter the amount you want to deposit to continue the transaction.",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                )

                // Rule and notification
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "check icon",
                            tint = Color.Green
                        )
                        Text(
                            text = "Minimum amount is 20,000 VND per transaction",
                            style = TextStyle(
                                color = Color.Green,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "cancel icon",
                            tint = Color.Red
                        )
                        Text(
                            text = "",
                            style = TextStyle(
                                color = Color.Green,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            // Accept button
            LinearButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                onClick = {
                    if(amountState.value < 20000L) {
                        viewModel.showToast("Please enter the amount you want to deposit bigger than 20,000 VND")
                    }
                    else{
                        viewModel.onGoToTransactionAcceptScreen(amountState.value)
                    }
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Continue",
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.DoubleArrow,
                        contentDescription = "continue icon",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}