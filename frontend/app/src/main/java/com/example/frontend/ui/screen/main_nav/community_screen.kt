package com.example.frontend.ui.screen.main_nav


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.main_nav.CommunityViewModel
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.GerneChipButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.theme.OrangeRed


@Composable
fun CommunityScreen(viewModel: CommunityViewModel = hiltViewModel()){
    val hotCommunityList = viewModel.hotCommunities.collectAsState()
    val communitiesFollowCategory= viewModel.communitiesFollowCategory.collectAsState()
    val category = viewModel.category.collectAsState()
    val isLoadingHotCommunities= viewModel .isLoadingHotCommunities.collectAsState()
    val isLoadingCommunitiesFollowCategory= viewModel .isLoadingCommunitiesFollowCategory.collectAsState()

    ScreenFrame(
        topBar = {
            TopBar(
                title = "Community",
                showBackButton = false,
                iconType = "Setting",
                onLeftClick = {viewModel.onGoToNotificationScreen()},
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(category.value) { item ->
                    GerneChipButton(
                        genre = item,
                        onClick = {viewModel.fetchCommunitiesFollowCategory(item.id)}
                    )
                }
            }

            SectionTitle(title = "Find Communities", modifier = Modifier.padding(start = 20.dp))

            //community follow category
            if (isLoadingCommunitiesFollowCategory.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeRed)
                }

            }
            else if(communitiesFollowCategory.value.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                ){
                    Text(
                        text = "No communities found.",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = androidx.compose.ui.graphics.Color.White
                        )

                    )
                }
            }
            else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                ) {

                    items(communitiesFollowCategory.value) {item->
                        CommunityCard(
                            model = item,
                            onClick = {viewModel.onGoToCommunityDetailScreen(item.id)}
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            SectionTitle(title = "Hot Communities", modifier = Modifier.padding(start = 20.dp))

            if (isLoadingHotCommunities.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeRed)
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                ) {

                    items(hotCommunityList.value) { item->
                        CommunityCard(
                            model = item,
                            onClick = {viewModel.onGoToCommunityDetailScreen(item.id)}
                        )

                    }
                }
            }

        }
    }


}
