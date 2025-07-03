package com.example.frontend.ui.screen.transaction

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.frontend.data.model.Role
import com.example.frontend.data.model.User
import com.example.frontend.presentation.viewmodel.transaction.WalletDetailViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import java.math.BigDecimal

@Composable
fun WalletDetailScreen(viewModel: WalletDetailViewModel= hiltViewModel()){
   val transactionList by viewModel.transactionList.collectAsState()
   val user = viewModel.user.collectAsState()

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Your Wallet",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
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
                    text = viewModel.formatMoney(user.value?.wallet?.toLong()?: 0L),
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
                    onClick = {viewModel.onGoToWithDraw() }

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
                    onClick = { viewModel.onGoToDepositScreen() }

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
                    items(transactionList.size) { index ->

                        NotificationCard(
                            content = when(transactionList[index].type){
                                "deposit" -> {
                                     "You successfully deposited ${viewModel.formatMoney(transactionList[index].money.toLong())} into your wallet."
                                }
                                "withdraw" ->{
                                     "You successfully withdrawn ${viewModel.formatMoney(transactionList[index].money.toLong())} from your wallet."
                                }
                                "purchase" ->{
                                    "You successfully purchased a chapter with ${viewModel.formatMoney(transactionList[index].money.toLong())}. "
                                }
                                "premium" -> "you have join premium at ${transactionList[index].time}"
                                else -> "Unknown transaction."

                            },
                            type = transactionList[index].type,
                            time = transactionList[index].time
                        )
                    }

                }
            }

        }
    }
}

val demoUser_wallet =  User(
    id = 1,
    name = "Peneloped Lyne",
    role = Role(1,"User"),
    dName = "tolapenelopee",
    backgroundId = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
    mail = "peneloped@gmail.com",
    followerNum = 200,
    novelsNum = 50,
    readListNum = 3,
    about = "Your membership starts as soon as you set up payment and subscribe. " +
            "Your monthly charge will occur on the last day of the current billing period. " +
            "We'll renew your membership for you can manage your subscription or turn off " +
            "auto-renewal under accounts setting.\n" +
            "By continuing, you are agreeing to these terms. See the private statement and restrictions.",
    wallet = BigDecimal(500.00),
    dob = "2020-03-12",
    isPremium = true

)