package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
//import com.example.frontend.ui.theme.BlueGray
import com.example.frontend.ui.theme.OrangeRed


@Composable
fun CommunityCard(item:String){

    Column (

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .width(110.dp)
            .background(Color.Black, RoundedCornerShape(10.dp))
            .padding(vertical = 12.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "community avatar",
            modifier = Modifier
                .size(60.dp,60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // Cắt ảnh nếu cần thiết để lấp đầy không gian
        )
        Text(
            text =item,
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding( vertical = 4.dp,horizontal = 10.dp)
        )
        Button(
            onClick = { /*TODO*/ },
            colors =  ButtonDefaults.buttonColors(
                containerColor = OrangeRed
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(20.dp)
        ) {
            Text(
                text =item,
                color = Color.Black,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding( horizontal = 8.dp)
            )

        }
        Text(
            text ="150k menbers",
            color = Color.White,
            style = TextStyle(
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .padding( vertical = 4.dp,horizontal = 10.dp)
        )

    }
}