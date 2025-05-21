package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import kotlinx.coroutines.delay

data class BannerItem(
    val imageRes: Int,
    val title: String = "",
    val onClick: () -> Unit = {}
)

@Composable
fun AutoScrollBanner(
    modifier: Modifier = Modifier,
    items: List<BannerItem> = emptyList(),
    autoScrollInterval: Long = 3000
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { items.size })
    var autoScrollEnabled by remember { mutableStateOf(true) }

    // Auto-scroll logic
    LaunchedEffect(pagerState.currentPage, autoScrollEnabled) {
        while (autoScrollEnabled) {
            delay(autoScrollInterval)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.65f)
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount =1,
            modifier = Modifier
                .fillMaxWidth()
            //    .clip(RoundedCornerShape(12.dp))
        ) { page ->
            val item = items[page]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        item.onClick()
                        autoScrollEnabled = false // Pause on click
                    }
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(2f)
                        .align(Alignment.TopCenter)
                )
            }
        }

        // Indicator
        Indicator(
            state = pagerState,
            count = items.size,
            size = 5.dp,
            selectedIndexColor = Color.White,
        unselectedColor = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)

        )

    }
}

@Preview
@Composable
fun BannerPreview() {
    val bannerItems = listOf(
        BannerItem(
            imageRes = R.drawable.banner1,
            title = "Truyện mới cập nhật",
            onClick = { /* Handle click */ }
        ),
        BannerItem(
            imageRes = R.drawable.banner2,
            title = "Top truyện đọc nhiều",
            onClick = { /* Handle click */ }
        ),
        BannerItem(
            imageRes = R.drawable.banner3,
            title = "Khuyến mãi đặc biệt",
            onClick = { /* Handle click */ }
        )
    )

    Column {
        AutoScrollBanner(items = bannerItems)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}