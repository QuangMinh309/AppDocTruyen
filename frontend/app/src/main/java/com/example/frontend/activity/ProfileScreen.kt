package com.example.frontend.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R

@Composable
fun AdvancedProfile(
    backgroundImageUrl: String = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
    avatarImageUrl: String = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
    name: String = "Peneloped Lynne",
    username: String = "@tolapenelope",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF2F1B24))
    ){
        Spacer(modifier = Modifier.height(30.dp))

        //TitleBar
        TitleBar()
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            // Background Image
            AsyncImage(
                model = backgroundImageUrl,
                contentDescription = "Profile background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(30.dp)),
                placeholder = ColorPainter(Color(0xFFE0E0E0)),
                error = ColorPainter(Color(0xFFBDBDBD))
            )

            // Gradient overlay
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            Color.Black.copy(alpha = 0.5f)
//                        ),
//                        startY = 100f
//                    )
//                )
//        )

            // Profile content
            Row(
                modifier = Modifier
                    .height(270.dp)
                    .padding(top = 160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with offset and transparent border
                Box(
                    modifier = Modifier
                        //    .offset(x = (50).dp) // Dịch sang trái 20dp
                        . padding(start=50.dp)
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(
                            width = 10.dp, // Độ dày của vòng tròn bao quanh
                            color = Color(0xFF2F1B24), // Vòng tròn trong suốt
                            shape = CircleShape
                        ),

                    ) {
                    AsyncImage(
                        model = avatarImageUrl,
                        contentDescription = "Profile avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color(0xFFEEEEEE)),
                        error = ColorPainter(Color(0xFFE0E0E0))
                    )
                }

                // Name and Username
                Column(
                    modifier = Modifier
                        .padding(start = 0.dp, top = 50.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFF76800), // Màu xanh lam đậm
                                    Color(0xFFDF4258)  // Màu xanh lam nhạt hơn
                                )
                            ),
                            shape = RoundedCornerShape(30.dp) // Bo góc 8dp
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Yellow,
                            shape = RoundedCornerShape(30.dp) // Bo góc khớp với background
                        )
                        .padding(start=20.dp,top=8.dp,end=20.dp, bottom = 8.dp), // Padding bên trong để chữ không dính sát viền
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Text(
                        text = name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = username,
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
// Thông tin  number
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier= Modifier.width(100.dp))
            StatItem(value = "200", label = "Followers")
            StatItem(value = "50", label = "Novels")
            StatItem(value = "12", label = "ReadList")
        }
        // Email
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0A2646), // Màu xanh lam
                            Color(0xFF14488E)  // Màu xanh nhạt
                        )
                    ),
                    shape= RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        )
        {
            InforItem(R.drawable.email_ic,"Abc@gmail.com")
            Spacer(modifier= Modifier.height(8.dp))
            InforItem(R.drawable.birthday_ic,"04/09/2005")

        }
        AboutSection()
        ReadListItem(item= ReadListItem_)

    }

}

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    title: String = "About",
    content: String = "Your membership starts as soon as you set up payment and subscribe. " +
            "Your monthly charge will occur on the last day of the current billing period. " +
            "We'll renew your membership for you can manage your subscription or turn off " +
            "auto-renewal under accounts setting.\n\n" +
            "By continuing, you are agreeing to these terms. See the private statement and restrictions."
) {
    Column(modifier = modifier.padding(16.dp)) {
        // Tiêu đề "About"
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Đường phân cách
        GradientDivider()

        // Nội dung
        Text(
            text = buildAnnotatedString {
                // Đoạn 1
                append(content.substringBefore("\n\n"))

                // Thêm khoảng cách giữa 2 đoạn
                append("\n\n")

                // Đoạn 2 với style khác
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(content.substringAfter("\n\n").trim())
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = Color.Gray
        )
    }
}
@Composable
fun GradientDivider() {
    Box(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .width(100.dp)
            .height(2.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF76800), // Màu xanh lá
                        Color(0xFF294C74)  // Màu xanh dương
                    )
                )
            )
    )
}
@Composable
fun InforItem(icon:Int,value: String)
{

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint= Color.Unspecified
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text=value,
            color= Color.White,
            fontSize = 20.sp
        )
    }

}
@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "$value",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray
            )
        )
    }
}

@Preview
@Composable
fun AdvancedProfilePreview() {
    MaterialTheme {
        AdvancedProfile(
            backgroundImageUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg",
            avatarImageUrl = "https://vcdn1-giaitri.vnecdn.net/2022/09/23/-2181-1663929656.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=apYgDs9tYQiwn7pcDOGbNg"
        )
    }
}