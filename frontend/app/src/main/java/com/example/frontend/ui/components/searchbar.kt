package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.OrangeRed


@Composable
fun SearchBar(
              value: String = "",
              onValueChange:(String)->Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){

        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .background(Color(0xA6747373), shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ){

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = OrangeRed,

                )

            BasicTextField(
                value =  value,
                onValueChange =  onValueChange,
                textStyle = TextStyle(
                    color = Color.Gray
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                    innerTextField()
                }

            )
        }
        Text(
            text = "Cancel",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp
            )
        )
    }

}