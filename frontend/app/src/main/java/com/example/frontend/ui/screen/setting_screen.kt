package com.example.frontend.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.SettingViewModel
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed


@Preview
@Composable
fun PreViewSeetingScreen(){
    val fakeviewmodel=SettingViewModel(NavigationManager())
    SettingScreen(fakeviewmodel)
}
@Composable
fun SettingScreen( viewModel: SettingViewModel= hiltViewModel()){
    val scrollState = rememberScrollState()
    ScreenFrame(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                // Back button
                Button(
                    onClick = {viewModel.onGoBack()},
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

                Text(
                    text = "Setting",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(0.33f))
            }
        }
    ){
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
            .verticalScroll(scrollState) // scroll ability,
        ){
            Text(
                text = "Profile",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            )

            //avatar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){

                Image(
                    painter = painterResource(id = R.drawable.intro_page1_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // fill mode
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        text = "Avatar",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Tap to change",
                        color = Color.White,
                        style =TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )



            //background
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){

                Image(
                    painter = painterResource(id = R.drawable.intro_page1_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp),
                    contentScale = ContentScale.Crop // fill mode
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        text = "Background",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Tap to change",
                        color = Color.White,
                        style =TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )


            //Display Name
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Display Name",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "@tolapeneloped",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }


            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )


            //Date of birth
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Date of birth",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "18 / 8 / 2005",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )

        }
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        ){
            Text(
                text = "Account & Security",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            )

            //userName
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "UserName",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "PeneLoped Lynne",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }


            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )


            //Mail
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Mail",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "sadsdkln@gmail.com",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )


            //Password
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Password",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "************",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )

            //Wallet
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Wallet",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "300,000đ",
                    color = Color.White,
                    style =TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                thickness = 1.dp,
                color = Color(0xff202430)
            )
        }

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        ) {
            Text(
                text = "Premium",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BurntCoral,OrangeRed
                        ),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    )
                ),
            )

            //Registration Date
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Registration Date",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "20 / 4 / 2025",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )

            }
        }
    }


}
