package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.data.model.Category
import com.example.frontend.ui.theme.OrangeRed
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun GenreTags(
    genres: List<Category>,
    fontSize: Float = 13.5f,
    horizontalPadding: Int = 1,
    verticalPadding: Int = 1,
    mainAxisSpacing: Int = 1,
    crossAxisSpacing: Int = 1,
    cornerRadius: Int = 30
) {
    val maxTags = 4 // Giả sử tối đa 6 thẻ để gần đúng 2 dòng (3 thẻ mỗi dòng)
    FlowRow(
        mainAxisSpacing = mainAxisSpacing.dp,
        crossAxisSpacing = crossAxisSpacing.dp
    ) {
        val displayedGenres = genres.take(maxTags)
        displayedGenres.forEach { tag ->
            Box(
                modifier = Modifier
                    .background(OrangeRed, RoundedCornerShape(cornerRadius.dp))
                    .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
                    .clickable { }
            ) {
                Text(
                    text = tag.name ?: "",
                    color = Color.Black,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        if (genres.size > maxTags) {
            Box(
                modifier = Modifier
                    .background(OrangeRed, RoundedCornerShape(cornerRadius.dp))
                    .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
                    .clickable { }
            ) {
                Text(
                    text = "...",
                    color = Color.Black,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SmallGenreTags(genres: List<Category>) {
    GenreTags(
        genres,
        fontSize = 10f,
        horizontalPadding = 10,
        verticalPadding = 4,
        mainAxisSpacing = 9,
        crossAxisSpacing = 7


    )
}

@Composable
fun LargeGenreTags(genres: List<Category>) {
    GenreTags(genres,
        fontSize = 14f,
        horizontalPadding = 15,
        verticalPadding = 8,
        mainAxisSpacing = 11,
        crossAxisSpacing = 19

    )
}
