package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.unit.sp
import com.example.frontend.R


@Composable
fun HeaderBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("< Back", color = Color.White, fontSize = 23.sp, fontFamily = FontFamily(Font(R.font.reemkufifun_wght)))
        Row {
            Icon(
                painterResource(R.drawable.setting_icon),
                contentDescription = "Setting Icon",
                tint = Color.White
            )
        }
    }
}
