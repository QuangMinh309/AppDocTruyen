package com.example.frontend.ui.screen.transaction



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.transaction.WithDrawViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.WithdrawRadioOption


@Preview
@Composable
fun PreViewWithDrawScreen(){
    val fakeviewmodel= WithDrawViewModel(NavigationManager())
    WithdrawScreen(fakeviewmodel)
}
@Composable
fun WithdrawScreen(viewmodel: WithDrawViewModel= hiltViewModel()){
    val selectedOption = rememberSaveable { mutableStateOf("full") }
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
        ){
            //main box
            Column (
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier
                    .padding(vertical = 66.dp, horizontal = 20.dp)
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
                        selected = selectedOption.value == "partial",
                        onSelect = { /*TODO*/ }

                    )
                    WithdrawRadioOption(
                        title = "Withdraw all",
                        subtitle = "2,000,000₫",
                        selected = selectedOption.value == "full",
                        onSelect = { /*TODO*/ }

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
                       value =  "Enter account number",
                       onValueChange =  {},
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
                               /*if (value.isEmpty()) {*/
                               Text(
                                   text = "",
                                   color = Color.Gray,
                                   fontSize = 16.sp
                               )
                           }
                           innerTextField()
                       }

                   )
                   //Account holder name box
                   BasicTextField(
                       value =  "Account holder name",
                       onValueChange =  {},
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
                               /*if (value.isEmpty()) {*/
                               Text(
                                   text = "",
                                   color = Color.Gray,
                                   fontSize = 16.sp
                               )
                           }
                           innerTextField()
                       }

                   )
                   //Bank name box
                   BasicTextField(
                       value =  "Bank name",
                       onValueChange =  {},
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
                               /*if (value.isEmpty()) {*/
                               Text(
                                   text = "",
                                   color = Color.Gray,
                                   fontSize = 16.sp
                               )
                           }
                           innerTextField()
                       }

                   )
               }
            }
            LinearButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                onClick = { /*TODO*/ }

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