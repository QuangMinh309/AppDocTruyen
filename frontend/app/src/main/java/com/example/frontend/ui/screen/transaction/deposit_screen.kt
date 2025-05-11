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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import androidx.compose.foundation.layout.Box as Box1


@Preview
@Composable
fun DepositScreen(){
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Deposite money",
                showBackButton = true,
                iconType = "Setting",
                onBackClick = { /*TODO*/ },
                onIconClick = { /*TODO*/ }
            )
        }
    ){

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){

            //deposit info
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 66.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    text ="Enter the amount you want to deposit",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                //money enter text box
                BasicTextField(
                    value =  "200,000",
                    onValueChange =  {},
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
                                modifier = Modifier.weight(1f)
                                    .wrapContentWidth(Alignment.End)
                            )
                        }
                        innerTextField()
                    }

                )

                //Suggested amount
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(0.8f)
                        .align(Alignment.End)
                ){
                    Box1(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(35.dp)
                            .weight(0.3f)
                            .background(Color.Transparent,RoundedCornerShape(5.dp))
                            .border(1.dp,Color.Gray,RoundedCornerShape(5.dp))
                            .clickable { /*TODO*/ }
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
                            .background(Color.Transparent,RoundedCornerShape(5.dp))
                            .border(1.dp,Color.Gray,RoundedCornerShape(5.dp))
                            .clickable { /*TODO*/ }
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
                            .background(Color.Transparent,RoundedCornerShape(5.dp))
                            .border(1.dp,Color.Gray,RoundedCornerShape(5.dp))
                            .clickable { /*TODO*/ }
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
                //rule and  notification
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
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
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
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


            //accept button
            LinearButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                onClick = { /*TODO*/ }

            ){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Continue",
                        color = Color.Black,
                        style =  TextStyle(
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