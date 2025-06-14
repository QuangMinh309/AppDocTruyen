package com.example.frontend.ui.screen.transaction

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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.transaction.DepositViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import java.text.DecimalFormat
import androidx.compose.foundation.layout.Box as Box1

@Preview
@Composable
fun PreViewDepositeScreen() {
    val fakeViewModel = DepositViewModel(NavigationManager())
    DepositScreen(fakeViewModel)
}

@Composable
fun DepositScreen(viewModel: DepositViewModel = hiltViewModel()) {
    // State để lưu giá trị số tiền dưới dạng Long
    val amountState = remember { mutableLongStateOf(200000L) }

    // Hàm định dạng số tiền thành chuỗi (ví dụ: 200000 -> "200,000")
    fun formatAmount(amount: Long): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount)
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Deposite money",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Deposit info
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
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
                    value = formatAmount(amountState.longValue), // Hiển thị số tiền đã định dạng
                    onValueChange = { newValue ->
                        // Chuyển đổi chuỗi nhập vào thành Long
                        val cleanedValue = newValue.replace(",", "").replace("đ", "")
                        val newAmount = cleanedValue.toLongOrNull() ?: amountState.longValue
                        amountState.longValue = newAmount
                    },
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 8.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "đ",
                                color = Color.Gray,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentWidth(Alignment.End)
                            )
                        }
                        innerTextField()
                    }
                )

                // Suggested amount
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.End)
                ) {
                    Box1(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(35.dp)
                            .weight(0.3f)
                            .background(Color.Transparent, RoundedCornerShape(5.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                            .clickable {
                                amountState.longValue = 50000L // Cập nhật giá trị Long
                            }
                    ) {
                        Text(
                            text = "+ 50,000đ",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Box1(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(35.dp)
                            .weight(0.3f)
                            .background(Color.Transparent, RoundedCornerShape(5.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                            .clickable {
                                amountState.longValue = 200000L // Cập nhật giá trị Long
                            }
                    ) {
                        Text(
                            text = "+ 200,000đ",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        )
                    }
                    Box1(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(35.dp)
                            .weight(0.3f)
                            .background(Color.Transparent, RoundedCornerShape(5.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                            .clickable {
                                amountState.longValue = 500000L // Cập nhật giá trị Long
                            }
                    ) {
                        Text(
                            text = "+ 500,000đ",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        )
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
                    // Kiểm tra số tiền tối thiểu
                    if (amountState.longValue >= 20000L) {
                        viewModel.onGoToTransactionAcceptScreen(amountState.longValue)

                    } else {
                        // TODO: Hiển thị thông báo lỗi (Toast/Snackbar) nếu số tiền < 20,000
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