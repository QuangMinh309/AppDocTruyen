package com.example.frontend.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun CommunityScreen(){
    val categoryList = listOf("Adventure","Fantastic", "Mystery", "Autobiography")
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Community",
                showBackButton = false,
                iconType = "Setting",
                onIconClick = { /*TODO*/ }
            )
        }
    ){
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
