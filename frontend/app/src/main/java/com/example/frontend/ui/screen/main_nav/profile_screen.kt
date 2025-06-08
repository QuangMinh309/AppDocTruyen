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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.ProfileViewModel
import com.example.frontend.ui.components.ReadListItem
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.formatViews
import com.example.frontend.ui.theme.BrightAquamarine
import com.example.frontend.ui.theme.BurntCoral
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed
import com.example.frontend.ui.theme.SteelBlue


@Preview(showBackground = true)
@Composable
fun PreviewScreenContent2() {
    val fakeViewModel = ProfileViewModel(NavigationManager())
    ProfileScreen(viewModel = fakeViewModel)
}
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel())
{
    val user by viewModel.user.collectAsState()
    val storyList  = viewModel.storyList
    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoToNotificationScreen() },
                onRightClick = { viewModel.onGoToSetting()}
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
                modifier = Modifier.fillMaxWidth()
            ) {
                // Background Image
                AsyncImage(
                    model = user.backgroundUrl,
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
                                width = 6.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(OrangeRed, BurntCoral)
                                ),
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center
                        ){
                            Box(modifier = Modifier
                                .clip(CircleShape)
                                .size(80.dp)
                                .border(
                                    width = 5.dp,
                                    color = DeepSpace,
                                    shape = CircleShape
                                )
                            ){
                                AsyncImage(
                                    model =  user.avatarUrl,
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
                                .padding(horizontal = 20.dp, vertical = 4.dp ),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            Text(
                                text =  user.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text =  "@${user.dName}",
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier= Modifier.weight(1f))
                StatItem(value = user.followerNum?:0, label = "Followers")
                StatItem(value = user.novelsNum?:0, label = "Novels")
                StatItem(value = user.readListNum?:0, label = "ReadList")
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
                InforItem(Icons.Outlined.Mail,user.mail?:"")
                Spacer(modifier= Modifier.height(8.dp))
                InforItem(Icons.Outlined.Cake,user.dob.toString())

            }
            AboutSection(content = user.about)
            //user readList
            Column (modifier = Modifier.fillMaxWidth()){
                SectionTitle(title = "StoryList")
                // Đường phân cách
                GradientDivider()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(storyList ) { item ->
                        ReadListItem(item = ReadListItem_, onClick = {viewModel.onGoToStoryScreen(item.id)})
                    }
                }
            }

        }
    }

}

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    title: String = "About",
    content: String?
) {
    Column(modifier = modifier) {
        // Tiêu đề "About"
        SectionTitle(title=title)
        // Đường phân cách
        GradientDivider()

        // Nội dung
        Text(
            text = buildAnnotatedString {
                append(content?.substringBefore("\n") ?:"No description available...")
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
fun StatItem(value: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Text(
            text = formatViews(value.toLong()),
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
