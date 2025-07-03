package com.example.frontend.ui.screen.transaction



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.transaction.TransactionAcceptViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar


@Composable
fun TransactionAcceptScreen(viewModel: TransactionAcceptViewModel= hiltViewModel()){
    // State để lưu giá trị số tiền dưới dạng Long
    val amountState = viewModel.amountState.collectAsState()
    val user by viewModel.user.collectAsState()


    val toast by viewModel.toast.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Transaction Accept",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){

        Box (
            modifier = Modifier.fillMaxSize()
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp)
                    .align(Alignment.TopCenter),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background_transaction_accept),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
                Column (
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.77f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    //info
                    Column (
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(20.dp),
                    ){
                        //transaction info
                        Column (
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                text ="Transaction information",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            //amount
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(
                                    text= "Amount to Transfer",
                                    style = TextStyle(
                                        color = Color.LightGray,
                                        fontSize = 16.sp,
                                    )
                                )
                                Text(
                                    text= viewModel.formatMoney(amountState.value.toLong()),
                                    style = TextStyle(
                                        color = Color.Green,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            //fee
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(
                                    text= "Service Fee",
                                    style = TextStyle(
                                        color = Color.LightGray,
                                        fontSize = 16.sp,
                                    )
                                )
                                Text(
                                    text= "0đ",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }


                        }

                        //Transfer Details
                        Column (
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                text ="Transfer Details",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            //account number
                            Text(
                                text= "Account Number: 37101010995044",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                )
                            )
                            //Account Holder
                            Text(
                                text= "Account Holder: Pham Ngoc Quang Minh",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                )
                            )
                            //Bank name
                            Text(
                                text= "Bank: MSB",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                )
                            )
                            //transaction message
                            Text(
                                text= "Message: Mã người dùng ${user?.id} nạp ${viewModel.formatMoney(amountState.value.toLong())}",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                )
                            )


                        }

                        Text(
                            text= "(Please check the information and enter the correct transfer message)",
                            style = TextStyle(
                                color = Color.Green,
                                fontSize = 14.sp,
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    //QR code
                    Image(
                        painter = painterResource(id = R.drawable.qr),
                        contentDescription = "community avatar",
                        modifier = Modifier
                            .size(160.dp),
                        contentScale = ContentScale.Crop // fill mode
                    )
                }
            }


            //accept button
            LinearButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomEnd),
                onClick = { viewModel.deposit() }

            ){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Complete",
                        color = Color.Black,
                        style =  TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

