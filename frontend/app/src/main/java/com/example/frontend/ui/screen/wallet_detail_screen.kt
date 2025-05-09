package com.example.frontend.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar

@Preview
@Composable
fun WalletDetailScreen(){
    val historyList = listOf("Bạn đã nạp 300.000₫ vào tài .","Bạn đã chi 300.000₫ để mua “Tempting the divine”.")
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Your Wallet",
                showBackButton = true,
                iconType = "Setting",
                onBackClick = { /*TODO*/ },
                onIconClick = { /*TODO*/ }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment =Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ){
            //balance info
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 66.dp, bottom = 46.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ){
                Text(
                    text = "Available balance",
                    color = Color.LightGray,
                    fontSize = 18.sp
                )
                Text(
                    text = "300,000đ",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            //wallet options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                LinearButton(
                    modifier = Modifier
                        .weight(0.4f)
                        .height(35.dp),
                    onClick = { /*TODO*/ }

                ){
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Filled.CreditScore,
                            contentDescription = "withdraw button icon" ,
                            tint = Color.Black
                        )
                        Text(
                            text = "Withdraw",
                            color = Color.Black,
                            style =  TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.2f))
                LinearButton(
                    modifier = Modifier
                        .weight(0.4f)
                        .height(35.dp),
                    backgroundColor = listOf(Color.LightGray,Color.LightGray),
                    onClick = { /*TODO*/ }

                ){
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Filled.Wallet,
                            contentDescription = "withdraw button icon" ,
                            tint = Color.Black
                        )
                        Text(
                            text = "Deposit",
                            color = Color.Black,
                            style =  TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }


                }

            }

            //transaction history
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp)
                    .background(Color.DarkGray.copy(alpha = 0.5f),shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 15.dp, vertical = 30.dp)
                ){
                Text(
                    text = "Recent Transactions",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily =  FontFamily(Font(R.font.reemkufifun_wght))
                )
                LazyColumn (
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                ){
                    items(historyList.size) { index ->
                        NotificationCard(
                            cardType = "transactionNotification",
                            transactionContent = historyList[index],
                            transactionType = if(index==0) "Recharge" else "Transfer",
                            time = "2 day ago"
                        )
                    }

                }
            }

        }
    }
}