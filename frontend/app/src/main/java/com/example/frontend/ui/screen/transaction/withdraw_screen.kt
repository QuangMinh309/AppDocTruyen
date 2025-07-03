package com.example.frontend.ui.screen.transaction



import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.transaction.WithDrawViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.WithdrawRadioOption



@Composable
fun WithdrawScreen(viewmodel: WithDrawViewModel= hiltViewModel()){

    val amountState = viewmodel.amountState.collectAsState()
    val amountTextState = rememberSaveable() { mutableStateOf("") }
    val selectedOption by viewmodel.selectedOption.collectAsState()

    val accountNumber by viewmodel.accountNumber.collectAsState()
    val accountHolderName by viewmodel.accountHolderName.collectAsState()
    val bankName by viewmodel.bankName.collectAsState()
    val user by viewmodel.user.collectAsState()

    val toast by viewmodel.toast.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(toast) {
        toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewmodel.clearToast()
        }
    }

    LaunchedEffect(amountState.value) {
        amountTextState.value = viewmodel.formatMoney(amountState.value)
    }

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Withdraw money",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewmodel.onGoBack() },
                onRightClick = { viewmodel.onGoToSetting() }
            )
        }
    ){

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            //main box
            Column (
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 20.dp)
                    .background(Color(0xA6747373), shape = RoundedCornerShape(15.dp))
                    .padding(20.dp)
            ){
                //Withdrawal option
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        text ="Withdrawal Options",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    WithdrawRadioOption(
                        title = "Withdraw a partial amount",
                        subtitle = "Minimum withdrawal 20,000₫",
                        selected = selectedOption == "partial",
                        onSelect = {
                            viewmodel.changeSelectedOption("partial")
                            viewmodel.changeAmount(0L)
                        }

                    )
                    AnimatedVisibility(
                        visible = selectedOption == "partial",
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        BasicTextField(
                            value = amountTextState.value,
                            onValueChange = { newValue ->
                                // Làm sạch chuỗi nhập
                                val cleaned = newValue.replace("[^\\d]".toRegex(), "")
                                val newAmount = cleaned.toLongOrNull() ?: 0L

                                // Cập nhật Long và hiển thị format
                                viewmodel.changeAmount(newAmount)
                                amountTextState.value = viewmodel.formatMoney(newAmount)
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
                    }
                    WithdrawRadioOption(
                        title = "Withdraw all",
                        subtitle = viewmodel.formatMoney(user?.wallet?.toLong()?:0L),
                        selected = selectedOption== "full",
                        onSelect = {
                            viewmodel.changeSelectedOption("full")
                            viewmodel.changeAmount(user?.wallet?.toLong()?:0L)
                        }

                    )

                }

                //Account info
               Column(
                   verticalArrangement = Arrangement.spacedBy(15.dp),
               ) {
                   Text(
                       modifier = Modifier
                           .padding(bottom = 10.dp),
                       text ="Withdraw to",
                       style = TextStyle(
                           color = Color.White,
                           fontSize = 20.sp,
                           fontWeight = FontWeight.Bold
                       )
                   )

                   //account number box
                   BasicTextField(
                       value = accountNumber,
                       onValueChange =  {value -> viewmodel.changeAccountNumber(value)},
                       textStyle = TextStyle(
                           color = Color.White,
                           fontSize = 16.sp
                       ),
                       modifier = Modifier
                           .background(Color(0xA6747373), shape = RoundedCornerShape(5.dp))
                           .padding(vertical = 15.dp, horizontal = 8.dp),
                       singleLine = true,
                       decorationBox = { innerTextField ->
                           Row(modifier = Modifier.fillMaxWidth()) {
                               if (accountNumber.isEmpty()) {
                                   Text(
                                       text = "",
                                       color = Color.Gray,
                                       fontSize = 16.sp
                                   )
                               }
                           }
                           innerTextField()
                       }

                   )
                   //Account holder name box
                   BasicTextField(
                       value = accountHolderName ,
                       onValueChange =  {viewmodel.changeAccountHolderName(it)},
                       textStyle = TextStyle(
                           color = Color.Gray,
                           fontSize = 16.sp
                       ),
                       modifier = Modifier
                           .background(Color(0xA6747373), shape = RoundedCornerShape(5.dp))
                           .padding(vertical = 25.dp, horizontal = 8.dp),
                       singleLine = true,
                       decorationBox = { innerTextField ->
                           Row(modifier = Modifier.fillMaxWidth()) {
                               if (accountHolderName.isEmpty()) {
                               Text(
                                   text ="Account holder name" ,
                                   color = Color.Gray,
                                   fontSize = 16.sp
                               )
                               }
                           }
                           innerTextField()
                       }

                   )
                   //Bank name box
                   BasicTextField(
                       value = bankName,
                       onValueChange =  {viewmodel.changeBankName(it)},
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
                               if (bankName.isEmpty()) {
                                   Text(
                                       text = "Bank name" ,
                                       color = Color.Gray,
                                       fontSize = 16.sp
                                   )
                               }
                           }
                           innerTextField()
                       }

                   )
               }

            }
            LinearButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp),
                onClick = {
                    if(amountState.value < 20000L) {
                        viewmodel.showToast("The minimum amount to withdraw is 20,000 VND.Please enter the other number.")
                    }
                    else{
                        viewmodel.withdraw()
                    }
                }

            ){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Filled.Beenhere,
                        contentDescription = "confirm",
                        tint = Color.Black
                    )
                    Text(
                        text = "Confirm transaction",
                        color = Color.Black,
                        style =  TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}