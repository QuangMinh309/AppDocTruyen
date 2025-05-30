    package com.example.frontend.ui.screen.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed

@Preview
@Composable
fun ChattingScreen(){
    val messages = listOf( "sdagthoruaes99ig","Hello?","R.drawable.intro_page1_bg","A")
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .padding(
                WindowInsets.statusBars
                    .asPaddingValues()
            )
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Button(
                onClick = { /*TODO*/ },
                colors =  ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(25.dp)
                    .weight(0.3f)
                    .wrapContentWidth(Alignment.Start)
            ) {
                Text(
                    text = "< Back",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                    ),

                )
            }
            Text(
                text = "Food in anime",
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

            Icon(
                painter = painterResource(id = R.drawable.setting_icon),
                contentDescription = "Custom Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.3f)
                    .wrapContentWidth(Alignment.End)
            )
        }

        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            items(messages.size) { index ->
                if (index ==2) {
                    //ImageChatBubble(messages[index])
                   Column(
                       verticalArrangement = Arrangement.spacedBy(5.dp),
                       modifier = Modifier
                           .fillMaxWidth()
                   ){
                       Text(
                           text = "T2 20/4/2025",
                           style = TextStyle(
                               fontSize = 12.sp,
                               color = Color.White,
                           ),
                           modifier = Modifier
                               .align(Alignment.CenterHorizontally)
                               .padding(bottom = 10.dp)
                       )
                       Text(
                           text = "@peneloped Lynne",
                           style = TextStyle(
                               fontSize = 12.sp,
                               color = Color.White,
                           ),
                           modifier = Modifier
                               .padding(start = 50.dp)
                       )
                       Row(
                           horizontalArrangement = Arrangement.spacedBy(10.dp)
                       ){
                           Image(
                               painter = painterResource(id = R.drawable.intro_page1_bg),
                               contentDescription = "community avatar",
                               modifier = Modifier
                                   .size(40.dp)
                                   .clip(CircleShape),
                               contentScale = ContentScale.Crop // fill mode
                           )
                           Image(
                               painter = painterResource(id = R.drawable.intro_page1_bg),
                               contentDescription = "community avatar",
                               modifier = Modifier
                                   .width(150.dp)
                                   .clip(RoundedCornerShape(20.dp)),
                               contentScale = ContentScale.FillWidth
                           )

                       }
                   }
                }else if(index % 2==0){
                   // MyChatBubble(messages[index])
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()

                    ){
                        Text(
                            text = "@peneloped Lynne",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.White,
                            ),
                            modifier = Modifier
                                .padding(start = 50.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.intro_page1_bg),
                                contentDescription = "community avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop // fill mode
                            )

                            Box(
                                modifier = Modifier
                                    .background(Color(0x41485ACC), RoundedCornerShape(30.dp))
                                    .padding(vertical = 10.dp, horizontal = 25.dp)
                            ){
                                Text(
                                    text = messages[index],
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.White,
                                    )
                                )
                            }
                        }
                    }
                } else {
                   // TheirChatBubble(messages[index])
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp)
                    ){
                        Text(
                            text = "09:00 AM",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.White,
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 10.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .background(OrangeRed, RoundedCornerShape(30.dp))
                                    .padding(vertical = 10.dp, horizontal = 25.dp)
                            ){
                                Text(
                                    text = messages[index],
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.White,
                                    )
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.intro_page1_bg),
                                contentDescription = "community avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop // fill mode
                            )

                        }
                    }
                }
            }
        }

    }
}