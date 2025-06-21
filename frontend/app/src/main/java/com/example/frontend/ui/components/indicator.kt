package com.example.frontend.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SalmonRose

@Composable
fun Indicator(modifier: Modifier,
              state: PagerState,
              count: Int,
              size: Dp,
              selectedIndexColor: Color = OrangeRed,
              unselectedColor: Color = SalmonRose,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()

    ) {
        repeat(count) { index ->
            val isSelected = state.currentPage == index

            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) (size * 4) else size,
                label = "DotWidthAnimation"
            )

            val dotColor by animateColorAsState(
                targetValue = if (isSelected) selectedIndexColor else unselectedColor,
                label = "DotColorAnimation"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(size)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}