package com.example.frontend.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

@Composable
fun TitleBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)  // Tăng chiều cao lên 60dp cho cân đối
            .padding(horizontal = 16.dp),  // Thêm padding hai bên
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween  // Căn đều khoảng cách
    ) {
        // Icon thông báo bên trái
        Icon(
            painter = painterResource(id = R.drawable.notification_ic),
            contentDescription = "Notifications",
            tint = Color.White,
            modifier = Modifier
                .size(50.dp)  // Giảm kích thước icon
                .clickable { /* Xử lý click */ }
        )

        // Text chính giữa
        Text(
            text = "Good Morning !",
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 0.5.sp  // Thêm letter spacing
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Icon cài đặt bên phải
        Icon(
            painter = painterResource(id = R.drawable.setting_ic),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier
                .size(50.dp)
                .clickable { /* Xử lý click */ }
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(color = Color.White, fontSize = 18.sp),
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold
    )
}