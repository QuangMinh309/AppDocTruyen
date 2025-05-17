package com.example.frontend.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.transaction.PremiumViewmodel
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.SalmonRose

@Preview
@Composable
fun PreViewPremiumScreen()
{
    val fakeviewmodel=PremiumViewmodel(NavigationManager())
    PremiumScreen(fakeviewmodel)
}
@Composable
fun PremiumScreen(viewmodel: PremiumViewmodel= hiltViewModel())
{
//    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page2_bg)
//    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = DeepSpace,
            )
            .paint(
                painterResource(R.drawable.intro_page2_bg),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
        )
        {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Try Premium",
                fontSize = 32.sp,
                fontFamily = FontFamily(Font(R.font.abeezee_regular)),
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Try Premium with us to get infinite insights from every novel",
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.abeezee_regular)),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(350.dp))
            ElevatedButton(
                onClick = {
                    viewmodel.onGoToWalletDetailScreen()
                },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SalmonRose
                ),
            )
            {
                Text(
                    text = "Register",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Your membership starts as soon as you finish your registration. " +
                        "Depending on the type of membership, you will be charged on either a monthly or yearly basis. " +
                        "You can cancel your membership at any time. " +
                        "By registering, you are agreeing to our Terms. Contact us for more details.",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
                color = Color.LightGray
            )
        }
    }
}