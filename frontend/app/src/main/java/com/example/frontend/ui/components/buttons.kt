package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.data.model.Category
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun LinearButton(modifier: Modifier = Modifier,
                 onClick: () -> Unit = {},
                 backgroundColor: List<Color> = listOf(OrangeRed, BurntCoral),
                 content: @Composable () -> Unit ={}
){
    Button(
        onClick =  onClick,
        colors =  ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = backgroundColor,
                    ),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
           content()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGerneChipButton() {
    val fakeGenre = Category(id=2,name = "Action") // Tạo giả lập đối tượng

    GerneChipButton(
        genre = fakeGenre,
        onClick = {}
    )
}
@Composable
fun GerneChipButton (genre:Category,
                     onClick: () -> Unit={}) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = OrangeRed
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = Modifier
            .height(35.dp)
    ) {
        Text(
            text = genre.name?:"",
            color = Color.Black,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 10.dp)
        )
    }
}