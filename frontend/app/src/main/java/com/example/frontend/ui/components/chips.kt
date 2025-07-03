 package com.example.frontend.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.data.model.Category
import com.example.frontend.ui.theme.OrangeRed

 @Composable
fun Chip(
    text: String,
    onClick: () -> Unit = {}
) {
    Surface(
        color = Color.Unspecified,
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .clickable(
                onClick = {
                    Log.d("ChipDebug", "Chip clicked: $text")
                    onClick()
                }
            )
            .padding(horizontal = 10.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00FF99),
                        Color(0xFF004099)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
            // Đảm bảo kích thước tối thiểu để nhận sự kiện
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StoryChips(modifier: Modifier = Modifier,texts: List<Category>, onClick: (id:Int,name:String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Hot pic title with fire icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = "Hot pic",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp)
            )
            repeat(times = 3) {
                Icon(
                    painter = painterResource(id = R.drawable.popular_icon), // Thay bằng icon lửa nếu có
                    contentDescription = "Fire Icon",
                    modifier = Modifier
                        .size(18.dp)
                        .padding(start = 4.dp),
                    tint = OrangeRed
                )
            }

        }

        // chips in a FlowRow to wrap content
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            texts.forEach { genre ->
                Chip(text = genre.name?:"", onClick = {onClick(genre.id,genre.name.toString())})
            }
        }
    }
}

@Composable
fun GenreChip(modifier: Modifier = Modifier,genre: Category) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(20.dp)
            .widthIn(50.dp)
            .background(color = OrangeRed, shape = RoundedCornerShape(30.dp))
            .then(modifier)
    ) {
        Text(
            text =genre.name?:"...",
            color = Color.Black,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding( horizontal = 10.dp)
        )

    }

}

@Composable
fun SelectChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) Color(0xFFFBAD00) else Color(0xFFEA5C18)
    Surface(
        color = background,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = name,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontFamily = FontFamily(Font(R.font.reemkufifun_semibold))
        )
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    )
    {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            categories.forEach { category ->
                SelectChip(
                    name = category.name?:"",
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun SelectList(
    names: List<String>,
    selectedName: String?,
    onNameSelected: (String) -> Unit
)
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
    {
        names.forEach { name ->
            SelectChip(
                name = name,
                isSelected = name == selectedName,
                onClick = { onNameSelected(name) }
            )
        }
    }
}


