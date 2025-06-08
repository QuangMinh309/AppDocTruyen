package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.ReemKufifunFontFamily

@Composable
fun SectionTitle(
    iconResId: Int? =null, // Tham số mới để truyền ID của icon từ drawable
    modifier: Modifier = Modifier,
    title: String
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 18.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = title,
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),


           // Để text chiếm phần còn lại và đẩy icon sang phải
        )
        if(iconResId!=null) {
            // Hiển thị 3 icon giống nhau từ drawable
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Icon",
                modifier = Modifier.size(40.dp),
                tint= Color.Unspecified

            )
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Icon",
                modifier = Modifier.size(40.dp),
                tint= Color.Unspecified

            )
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Icon",
                modifier = Modifier.size(40.dp),
                tint= Color.Unspecified
            )
        }
    }
}