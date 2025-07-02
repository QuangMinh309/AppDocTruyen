package com.example.frontend.ui.screen.intro_authentication


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.intro_authentification.IntroViewModel
import com.example.frontend.ui.components.Indicator
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BrightBlue
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.ReemKufifunFontFamily
import com.example.frontend.ui.theme.SalmonRose
import com.example.frontend.ui.theme.SteelBlue

//@EntryPoint
//@InstallIn(SingletonComponent::class)
//interface ImageProviderEntryPoint {
//    fun imageUrlProvider(): ImageUrlProvider
//}
@Preview
@Composable
fun IntroScreen(viewModel: IntroViewModel = hiltViewModel()) {

    val pages = listOf<@Composable () -> Unit>(
        { Page1() },
        { Page2() },
        { Page3() },
        { Page4(onLoginClick = {viewModel.onGoToLoginScreen()}) }
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .padding(top = 27.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount =1,

        ) { page -> pages[page]() }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .height(370.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DeepBlue
                        )
                    )
                )
        )
        // Indicator
        Indicator(
            state = pagerState,
            count = pages.size,
            size = 8.dp,
            selectedIndexColor = OrangeRed,
            unselectedColor = SalmonRose,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)

        )

    }
}

@Preview
@Composable
fun Page1()
{
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page1_bg)
    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 40.dp)
        ) {            // Dòng chữ "WELCOME TO"
            Text(
                text = "WELCOME TO\n           OUR WORLD >>",
                style = TextStyle(
                    fontFamily = ReemKufifunFontFamily,
                    fontSize = 32.sp,
                    lineHeight = 50.sp,
                    fontWeight = FontWeight.Normal,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            OrangeRed, BrightBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),

                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            Image(
                //model = painterResource(R.drawable.intro_page1_bg),
                contentDescription = "Loaded image",
               painter = painterResource(R.drawable.intro_page1_bg),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                contentScale = ContentScale.FillWidth
            )

        }
    }
}

@Preview
@Composable
fun Page2()
{
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page2_bg)
    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .padding(start = 60.dp, end = 20.dp)
        ) {
            Text(
                text = "A repo with\n thousands of novels.",
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontFamily = ReemKufifunFontFamily,
                    fontSize = 32.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Normal,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            OrangeRed, BrightBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),

                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.intro_page2_bg),
                contentDescription = "Ảnh minh họa",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Preview
@Composable
fun Page3()
{
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page3_bg)
    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            buildAnnotatedString {
                withStyle (
                    style = ParagraphStyle(
                        lineHeight = 45.sp
                    )
                )
                {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = ReemKufifunFontFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = OrangeRed
                        )
                    ) {
                        append("Start creating \n     your \n           own  ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = ReemKufifunFontFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    OrangeRed, BrightBlue
                                ),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            )
                        )
                    ) {
                        append("STORIES...")
                    }
                }
            },
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.intro_page3_bg),
                contentDescription = "Ảnh minh họa",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                contentScale = ContentScale.FillWidth
            )
        }


    }
}

@Preview
@Composable
fun Page4(onLoginClick: () -> Unit = {})
{
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "LOGIN to join us!",
                style = TextStyle(
                    fontFamily = ReemKufifunFontFamily,
                    fontSize = 32.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Normal,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            OrangeRed, BrightBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    ),

                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
                .border(2.dp, Brush.linearGradient(
                    colors = listOf(
                        BrightAquamarine, SteelBlue
                    ),
                ),RoundedCornerShape(50))
            ,
            contentAlignment = Alignment.Center
        ){

            Button(
                onClick = {
                    onLoginClick()
                },
                modifier = Modifier
                    .size(width = 150.dp, height =45.dp)
                    .padding(3.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeRed
                )
            ) {
                Text(
                    text = "LOGIN",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                )
            }
        }
    }
}