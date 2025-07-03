package com.example.frontend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

@Preview
@Composable
fun TopBar(title: String = "",
              showBackButton: Boolean = true,
              iconType: String = "",
           unreadNotificationCount:Int=0,
              onLeftClick: () -> Unit = {},
              onRightClick: () -> Unit = {}){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp,vertical = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        // Back button
        if (showBackButton) {
            Button(
                onClick = onLeftClick,
                colors =  ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(25.dp)
                    .weight(0.33f)
                    .wrapContentWidth(Alignment.Start)
            ) {
                Text(
                    text = "< Back",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }
        }
        else{
            Icon(
                painter = painterResource(if (unreadNotificationCount == 0) R.drawable.notification_ic else R.drawable.notification) ,
                contentDescription = "Custom notification Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.33f)
                    .size(50.dp)
                    .wrapContentWidth(Alignment.Start)
                    .clickable { onLeftClick() }
            )
        }
        Text(
            text = title,
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(0.5f)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .align(Alignment.CenterVertically)
        )

        if(iconType == "Plus") {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Custom Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.33f)
                    .size(22.dp)
                    .wrapContentWidth(Alignment.End)
                    .clickable { onRightClick() }
            )
        }
        else{
            val drawableId= if (iconType == "Setting") {
                R.drawable.setting_config
            } else {
                R.drawable.search_normal
            }
            Icon(
                painter = painterResource(id = drawableId),
                contentDescription = "Custom Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.33f)
                    .size(22.dp)
                    .wrapContentWidth(Alignment.End)
                    .clickable { onRightClick() }
            )

        }


    }
}

