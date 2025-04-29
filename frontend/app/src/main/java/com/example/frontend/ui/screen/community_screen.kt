package com.example.frontend.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun CommunityScreen(){
    val categoryList = listOf("Adventure","Fantastic", "Mystery", "Autobiography")
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .padding( // ⭐ Kết hợp cả hai
                WindowInsets.statusBars
                    .asPaddingValues() // padding theo status bar
            )
            .padding(16.dp) // thêm 16.dp đều 4 cạnh
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Spacer(modifier = Modifier.weight(0.33f))
            Text(
                text = "Community",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(0.33f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .align(Alignment.CenterVertically)
            )
            Icon(
                painter = painterResource(id = R.drawable.setting_icon),
                contentDescription = "Custom Icon",
                tint = Color.White,
                modifier = Modifier
                    .weight(0.33f)
                    .wrapContentWidth(Alignment.End)

            )

        }


        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Hot Community",
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(vertical = 18.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryList) { item ->
                Button(
                    onClick = { /*TODO*/ },
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = OrangeRed),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(30.dp)
                ) {
                    Text(
                        text =item,
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                             .padding( vertical = 4.dp,horizontal = 10.dp)
                    )
                }


            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .height(height = 168.dp)
        ) {
            items(categoryList) { item ->
                CommunityCard(
                    item = item
                )

            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Recommended",
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(vertical = 18.dp)
        )


        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .height(height = 168.dp)
        ) {
            items(categoryList) { item ->
                CommunityCard(
                    item = item
                )

            }
        }


    }


}
