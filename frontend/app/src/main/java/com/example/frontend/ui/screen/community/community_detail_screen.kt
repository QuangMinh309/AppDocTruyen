package com.example.frontend.ui.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.community.CommunityDetailViewModel
import com.example.frontend.ui.components.LinearButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.components.formatViews
import com.example.frontend.ui.screen.main_nav.demoUser
import com.example.frontend.ui.theme.DeepSpace
import com.example.frontend.ui.theme.OrangeRed


//@Preview(showBackground = true)
//@Composable
//fun PreviewScreenContent5() {
//    val fakeViewModel = CommunityDetailViewModel(NavigationManager())
//    CommunityDetailScreen(viewModel = fakeViewModel)
//}

@Composable
fun CommunityDetailScreen(viewModel: CommunityDetailViewModel = hiltViewModel()){

    val scrollState = rememberScrollState()
    ScreenFrame(
        topBar = {
            TopBar(
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack()},
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            //Community info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 35.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){

                AsyncImage(
                    model = viewModel.community.avatarUrl,
                    contentDescription = "community avatar",
                    placeholder = painterResource(id = R.drawable.intro_page1_bg),
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // fill mode
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(
                        text = viewModel.community.name,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 24.sp,
                        )
                    )
                    Text(
                        text = "${formatViews(viewModel.community.memberNum.toLong())} members",
                        color = Color.White,
                        style =TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                    LinearButton(
                        modifier = Modifier
                             .fillMaxWidth()
                             .height(30.dp),
                        onClick = { viewModel.onGoToChattingScreen(viewModel.communityId)}

                    ){
                        Text(
                            text = "Join chat",
                            color = Color.Black,
                            style =  TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
            //Description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                SectionTitle(title = "Description")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState) // scroll ability

                    ){
//                        Text(
//                            text = viewModel.community.description,
//                            color = Color.White,
//                            style = TextStyle(
//                                fontSize = 14.sp
//                            ),
//                            modifier = Modifier.fillMaxWidth()
//
//                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DeepSpace),
                            ))
                            .align(Alignment.BottomCenter)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Arrow Drop Down",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(24.dp)
                    )
                }
            }

            //Members list
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.Start,
            ){
                SectionTitle(title = "Members")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.Transparent)
                ){
                        LazyRow (
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                        ){
                            items(viewModel.memberList) {item->
                                Column (
                                    verticalArrangement =Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.widthIn(max = 80.dp)
                                ){
                                    Spacer( modifier = Modifier.height(4.dp))
                                    AsyncImage(
                                        model = demoUser.avatarUrl,
                                        placeholder = painterResource(id = R.drawable.avt_img),
                                        contentDescription = "community avatar",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop // fill mode
                                    )
                                    Text(
                                        text ="@${item.dName}",
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                        color = Color.White,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            ),

                                    )
                                }
                            }
                        }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(170.dp)
                            .background(brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, DeepSpace),
                            ))
                            .align(Alignment.CenterEnd)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(80.dp)
                                .background(color = DeepSpace)
                                .align(Alignment.CenterEnd)
                        )
                        //view all button
                        Button(
                            onClick = { viewModel.onGoToSearchingMemberScreen(viewModel.communityId)},
                            colors =  ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                            ),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        shape = RoundedCornerShape(30.dp),
                                        color = DeepSpace
                                    )
                                .border(1.dp, OrangeRed, RoundedCornerShape(30.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "View all",
                                    color = OrangeRed,
                                    style = TextStyle(
                                        fontSize = 14.sp
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}