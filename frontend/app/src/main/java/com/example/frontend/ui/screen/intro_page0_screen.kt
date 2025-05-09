
package com.example.frontend.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.domain.ImageUrlProvider
import com.example.frontend.ui.theme.BrightBlue
import com.example.frontend.ui.theme.DeepBlue

import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SalmonRose


@Preview
@Composable
fun ImageUploadScreen(){
     var imgprovider = ImageUrlProvider()
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.intro_page1_bg)
    val ratio = imageBitmap.width.toFloat() / imageBitmap.height
    var imageUrl by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        imageUrl = imgprovider.fetchImage("intro_page3_bg_xmbse7");
    }

    val images = listOf(
        painterResource(id = R.drawable.intro_page1_bg),
        painterResource(id = R.drawable.intro_page2_bg),
        painterResource(id = R.drawable.intro_page3_bg)
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepSpace)
                .padding(top = 150.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 40.dp)
            ) {            // Dòng chữ "WELCOME TO"
                Text(
                    text = "WELCOME TO\n           OURS WORLD >>",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.reemkufifun_semibold)),
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

            Spacer(modifier = Modifier.height(50.dp)) // đẩy xa xuống
            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ){
                HorizontalPager(state = pagerState) { page ->
                    Image(
                        painter  = rememberAsyncImagePainter("https://res.cloudinary.com/dpqv7ag5w/image/upload/v1745405887/intro_page1_bg_c5eqxw.png") ,            //rememberAsyncImagePainter( imageUrl ),
                        contentDescription = "Ảnh minh họa",
                        modifier =Modifier.align(Alignment.BottomEnd)
                            .fillMaxWidth()
                            .aspectRatio(ratio),
                        contentScale = ContentScale.FillWidth
                    )
                }

                Box(
                    modifier = Modifier.run {
                        align(alignment = Alignment.BottomEnd)
                                        .fillMaxWidth()
                                        .height(450.dp)
                                        .background(Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                DeepBlue
                                            )
                                        ))
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    repeat(images.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (isSelected) 37.dp else 6.dp,6.dp)
                                .background(
                                    if (isSelected) OrangeRed
                                    else SalmonRose,
                                    shape = RoundedCornerShape(50.dp)
                                )
                        )
                    }
                }

            }
        }



}