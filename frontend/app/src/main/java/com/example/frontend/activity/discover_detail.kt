package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.R

@Preview
@Composable
fun DiscoverDetail()
{
    Column (
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF2F1B24))
    ){
        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchBar(modifier = Modifier.weight(1f) )
            Icon(
                painter = painterResource(id = R.drawable.setting_ic),
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)  // Giảm kích thước icon
                    .clickable { /* Xử lý click */ }
            )
        }
        GenreChips(genres = listOf(
            "Things We Never Got Over",
            "Twisted Love",
            "Twisted Love",
            "Iron Flame",
            "Things We Never Got Over",
            "Things We Never Got Over",
            "Echoes of Eternity",
            "The Silent Storm",
            "Whispers in the Dark",
            "Fires of Redemption"
        ))


    }


}
