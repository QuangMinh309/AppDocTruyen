package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.OrangeRed
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import com.example.frontend.ui.theme.BurntCoral

@Composable
fun StoryInfo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        // Background + gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.story_detail_page1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        alpha = 0.5f //Reduce the brightness of the image
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                OrangeRed.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }

        // Title of story
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = (-9).dp)
                .padding(start = 16.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                "Tempting the divine",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)))
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon 1: Sum of vote
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "200K",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.vote_icon),
                contentDescription = "Vote Icon",
                tint = OrangeRed,
                modifier = Modifier.then(
                    Modifier
                        .padding(start = 1.2.dp)
                        .size(with(LocalDensity.current) { 17.sp.toDp() })
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Icon 2: Sum of view
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "3.000",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                contentDescription = "View Icon",
                tint = Color.Unspecified,
                modifier = Modifier.then(
                    Modifier
                        .padding(start = 1.2.dp)
                        .size(with(LocalDensity.current) { 17.sp.toDp() })
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Icon 3: Chapter number
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "25",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght)))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.list_chapter_icon),
                contentDescription = "Number Chapter Icon",
                tint = Color.Unspecified,
                modifier = Modifier.then(
                    Modifier
                        .padding(start = 1.2.dp)
                        .size(with(LocalDensity.current) { 17.sp.toDp() })
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Button start read
        Button(
            onClick = {},
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(41.dp)
                .padding(start = 7.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(OrangeRed, Color(0xFFDF4258)),
                        endX = 200f
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 25.dp)
        ) {
            Text(
                text = "Start  read",
                color = Color.Black,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            )
        }
    }
}
