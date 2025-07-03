package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R

import com.example.frontend.presentation.viewmodel.story.StoryDetailViewModel

import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed

fun Text(text: String, color: Color, fontSize: TextUnit, fontWeight: FontWeight, any: Any?) {

}


@Composable
fun StoryInfo(viewModel:StoryDetailViewModel) {
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
            AsyncImage(
                model = viewModel.story.value?.coverImgUrl, // URL của hình ảnh
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        alpha = 0.5f // Giữ nguyên độ mờ
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
                    color = DeepSpace,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = viewModel.story.value?.name?:"",
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.weight(0.6f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon 1: Sum of vote
            Row(
                modifier = Modifier.weight(0.3f).wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatViews((viewModel.story.value?.voteNum?:0).toLong()),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
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
                modifier = Modifier.weight(0.3f).wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatViews((viewModel.story.value?.viewNum?:0).toLong()),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
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
                modifier = Modifier.weight(0.3f).wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatViews((viewModel.story.value?.chapterNum?:0).toLong()),
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    contentDescription = "Number Chapter Icon",
                    tint = OrangeRed,
                    modifier = Modifier.then(
                        Modifier
                            .padding(start = 1.2.dp)
                            .size(with(LocalDensity.current) { 17.sp.toDp() })
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

