package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.data.model.Chat
import com.example.frontend.ui.theme.OrangeRed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun ChatBubble(message: Chat) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
    ){
        //Time
        val formatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
        val daysDifference = ChronoUnit.DAYS.between(message.time, LocalDateTime.now())
        val dayOfWeek = message.time.format(formatter)
        androidx.compose.material3.Text(
            text =  if (daysDifference>1)
                        "$dayOfWeek ${message.time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
                     else message.time.format(DateTimeFormatter.ofPattern("hh:MM a")),

            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White,
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )

        //main content
        androidx.compose.material3.Text(
            text = "@${message.sender.dName}",
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
            //avatar
            AsyncImage(
                model = message.sender.avatarUrls.takeIf { !it.isNullOrEmpty()  }?:R.drawable.avt_img,
                contentDescription = "sender avatar",
                placeholder = painterResource(id = R.drawable.avt_img),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop // fill mode
            )
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                if(message.content != null){
                    Box(
                        modifier = Modifier.run {
                            background(Color(0x41485ACC), RoundedCornerShape(20.dp))
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                                .widthIn(max = 150.dp)
                        }
                    ){
                        androidx.compose.material3.Text(
                            text =  message.content,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        )
                    }
                }
                if(message.messagePicUrl!= null){
                    AsyncImage(
                        model = message.messagePicUrl.takeIf { it!="no picture." }?:R.drawable.broken_image,
                        contentDescription = "message pic",
                        placeholder = painterResource(id = R.drawable.broken_image),
                        modifier = Modifier
                            .widthIn(max = 150.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                }


            }


        }
    }
}

@Composable
fun MyChatBubble(message: Chat) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
    ){
        //Time
        val formatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
        val daysDifference = ChronoUnit.DAYS.between(message.time, LocalDateTime.now())
        val dayOfWeek = message.time.format(formatter)
        androidx.compose.material3.Text(
            text =  if (daysDifference>1)
                "$dayOfWeek ${message.time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            else message.time.format(DateTimeFormatter.ofPattern("hh:MM a")),

            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White,
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )

        //main content
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                    .fillMaxWidth(),

        ){
            //message
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End){
                if(message.content != null){
                    Box(
                        modifier = Modifier.run {
                            background(OrangeRed, RoundedCornerShape(20.dp))
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                                .widthIn(max = 150.dp)
                        }
                    ){
                        androidx.compose.material3.Text(
                            text =  message.content,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        )
                    }
                }
                    AsyncImage(
                        model = message.messagePicUrl.takeIf { it =="no picture." }?:R.drawable.broken_image,
                        contentDescription = "message pic",
                        placeholder = painterResource(id = R.drawable.broken_image),
                        modifier = Modifier
                            .widthIn(max = 150.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.FillWidth
                    )


            }
        }
    }
}