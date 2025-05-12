package com.example.frontend.ui.screen.main_nav

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Mail
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SteelBlue

@Composable
fun AdvancedProfile(
    backgroundImageUrl: String,
    avatarImageUrl: String ,
    name: String,
    nickName: String,
    modifier: Modifier = Modifier
) {
    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = { /*TODO*/ },
                onRightClick = { /*TODO*/ }
            )
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){

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
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    placeholder = painterResource(id = R.drawable.broken_image),
                    error = ColorPainter(Color(0xFFBDBDBD))
                )

                // Profile content
                Row(
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth()
                        .padding(top = 110.dp,start = 30.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Avatar with offset and transparent border
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(
                                width = 6.dp,
                                color = DeepSpace,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center

                        ) {
                        Box(modifier = Modifier
                                .clip(CircleShape)
                                .size(90.dp)
                            .border(
                                width = 6.dp, // Độ dày của vòng tròn bao quanh
                                brush = Brush.linearGradient(
                                    colors = listOf(OrangeRed, BurntCoral)
                                ), // Vòng tròn trong suốt
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center
                        ){
                            Box(modifier = Modifier
                                .clip(CircleShape)
                                .size(80.dp)
                                .border(
                                    width = 5.dp, // Độ dày của vòng tròn bao quanh
                                    color = DeepSpace,
                                    shape = CircleShape
                                )
                            ){
                                AsyncImage(
                                    model = avatarImageUrl,
                                    contentDescription = "Profile avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.avt_img),
                                    error = ColorPainter(Color(0xFFE0E0E0))
                                )
                            }
                        }
                    }

                    // Name and nickName
                    Box(
                        modifier = Modifier
                            .padding(start = 0.dp, top = 50.dp)
                            .border(2.dp, Brush.linearGradient(
                            colors = listOf(
                                BrightAquamarine, SteelBlue
                            ),
                        ),RoundedCornerShape(50))
                            .background(color = DeepSpace)
                            .padding(5.dp),
                        contentAlignment = Alignment.Center

                    ){
                        Column(
                            modifier = Modifier

                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            OrangeRed, BurntCoral
                                        )
                                    ),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 4.dp ), // Padding bên trong để chữ không dính sát viền
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            Text(
                                text = name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = nickName,
                                style = TextStyle(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
            // Thông tin  number
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier= Modifier.weight(1f))
                StatItem(value = "200", label = "Followers")
                StatItem(value = "50", label = "Novels")
                StatItem(value = "12", label = "ReadList")
            }

            // Email and dob
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0A2646), // Màu xanh lam
                                Color(0xFF14488E)  // Màu xanh nhạt
                            )
                        ),
                        shape= RoundedCornerShape(15.dp)
                    )
                    .padding(15.dp)
            )
            {
                InforItem(Icons.Outlined.Mail,"Abc@gmail.com")
                Spacer(modifier= Modifier.height(8.dp))
                InforItem(Icons.Outlined.Cake,"04/09/2005")

            }
            AboutSection()
            ReadListItem(item= ReadListItem_)

        }
    }

}

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    title: String = "About",
    content: String = "Your membership starts as soon as you set up payment and subscribe. " +
            "Your monthly charge will occur on the last day of the current billing period. " +
            "We'll renew your membership for you can manage your subscription or turn off " +
            "auto-renewal under accounts setting.\n" +
            "By continuing, you are agreeing to these terms. See the private statement and restrictions."
) {
    Column(modifier = modifier) {
        // Tiêu đề "About"
        SectionTitle(title=title)


        // Đường phân cách
        GradientDivider()

        // Nội dung
        Text(
            text = buildAnnotatedString {
                append(content.substringBefore("\n"))
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
fun InforItem(icon : ImageVector, value: String)
{

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint= OrangeRed
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text=value,
            color= Color.White,
            fontSize = 14.sp
        )
    }

}
@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Text(
            text = value,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
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
            avatarImageUrl = "",
            name = "Peneloped Lyne",
            nickName= "tolapeneloped",
        )
    }
}