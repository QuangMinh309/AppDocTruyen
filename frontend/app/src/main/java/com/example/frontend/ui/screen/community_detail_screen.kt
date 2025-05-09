package com.example.frontend.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun CommunityDetailScreen(){

    val memberList = listOf("member1","member2","member3")
    val scrollState = rememberScrollState()
    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = true,
                iconType = "Setting",
                onBackClick = { /*TODO*/ },
                onIconClick = { /*TODO*/ }
            )
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            //Community info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 35.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){

                Image(
                    painter = painterResource(id = R.drawable.intro_page1_bg),
                    contentDescription = "community avatar",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // fill mode
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        text = "Food in anime",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 24.sp,
                        )
                    )
                    Text(
                        text = "150K members",
                        color = Color.White,
                        style =TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                    LinearButton(
                        modifier = Modifier
                             .fillMaxWidth()
                             .height(30.dp),
                        onClick = { /*TODO*/ }

                    ){
                        Text(
                            text = "Join chat",
                            color = Color.Black,
                            style =  TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }

            //Description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Description",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState) // scroll ability

                    ){
                        Text(
                            text = "A group for everyone to talk about food seen in Anime or manga."
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                                    + "A group for everyone to talk about food seen in Anime or manga"
                            ,
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.fillMaxWidth() // đảm bảo text chiếm hết chiều ngang

                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DeepSpace),
                            ))
                            .align(Alignment.BottomCenter)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Arrow Drop Down",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(24.dp)
                    )
                }
            }

            //Members list
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Members",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.Transparent)
                ){
                        LazyRow (
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                        ){
                            items(memberList) {item->
                                Column (
                                    verticalArrangement =Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Spacer( modifier = Modifier.height(4.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.intro_page1_bg),
                                        contentDescription = "community avatar",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop // fill mode
                                    )
                                    Text(
                                        text ="@$item",
                                        color = Color.White,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            )
                                    )
                                }
                            }
                        }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(220.dp)
                            .background(brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, DeepSpace),
                            ))
                            .align(Alignment.CenterEnd)
                    ){
                        Button(
                            onClick = { /*TODO*/ },
                            colors =  ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                            ),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        shape = RoundedCornerShape(30.dp),
                                        color = Color.Transparent
                                    )
                                .border(1.dp, OrangeRed, RoundedCornerShape(30.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "View all",
                                    color = OrangeRed,
                                    style = TextStyle(
                                        fontSize = 14.sp
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}