package com.example.frontend.ui.screen


import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.frontend.R
import com.example.frontend.activity.LoginActivity
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import com.example.frontend.data.util.ImageUrlProvider
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BrightBlue
import com.example.frontend.ui.theme.DeepBlue
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SalmonRose
import com.example.frontend.ui.theme.SteelBlue
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageProviderEntryPoint {
    fun imageUrlProvider(): ImageUrlProvider
}
@Preview
@Composable
fun IntroScreen() {

    val pages = listOf<@Composable () -> Unit>(
        { Page1() },
        { Page2() },
        { Page3() },
        { Page4() }
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,

        ) { page -> pages[page]() }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index

                val dotWidth by animateDpAsState(
                    targetValue = if (isSelected) 36.dp else 8.dp,
                    label = "DotWidthAnimation"
                )

                val dotColor by animateColorAsState(
                    targetValue = if (isSelected) OrangeRed else SalmonRose,
                    label = "DotColorAnimation"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(dotWidth)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }
    }
}

@Preview
@Composable
fun Page1()
{
    val context = LocalContext.current.applicationContext
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(
            context,
            ImageProviderEntryPoint::class.java
        )
    }
    val imageProvider = remember { entryPoint.imageUrlProvider() }

    var imageUrl by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) } // 👉 thêm dòng này


    LaunchedEffect("intro_page3_bg_xmbse7") {
        val result = imageProvider.fetchImage("intro_page3_bg_xmbse7")
        result
            .onSuccess { url -> imageUrl = url }
            .onFailure { ex -> errorMessage = ex.message }
    }
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page1_bg)
    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 40.dp)
        ) {            // Dòng chữ "WELCOME TO"
            Text(
                text = "WELCOME TO\n           OUR WORLD >>",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
                    fontSize = 32.sp,
                    lineHeight = 50.sp,
                    fontWeight = FontWeight.SemiBold,
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
                painter = painterResource(id = R.drawable.intro_page1_bg),//rememberAsyncImagePainter( imageUrl ),
                contentDescription = "Ảnh minh họa",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DeepBlue
                            )
                        )
                    )
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
            .fillMaxSize()
            .background(DeepSpace),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .padding(start = 80.dp)
        ) {
            Text(
                text = "A repo with\n thousands of novels.",
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
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
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DeepBlue
                            )
                        )
                    )
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
            .fillMaxSize()
            .background(DeepSpace),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
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
                            fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = OrangeRed
                        )
                    ) {
                        append("Start creating \n       your \n                 own  ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
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
    }
}

@Preview
@Composable
fun Page4()
{
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DeepSpace,
                        DeepBlue
                    ),
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "LOGIN to join us!",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.reemkufifun_variablefont_wght)),
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
                    .padding(bottom = 8.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val intent =Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.size(width = 146.dp, height = 38.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Brush.linearGradient(
                colors = listOf(
                    BrightAquamarine, SteelBlue
                ),
                start = Offset(0f, 0f),
                end = Offset.Infinite
            )),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeRed
            )
        ) {
            Text(
                text = "LOGIN",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_bold)),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                modifier = Modifier
                    .size(width = 140.dp, height = 34.dp)
            )
        }
    }
}