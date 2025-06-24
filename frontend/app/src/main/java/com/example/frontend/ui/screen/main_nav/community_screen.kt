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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.main_nav.CommunityViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.GerneChipButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar



@Composable
fun CommunityScreen(viewModel: CommunityViewModel = hiltViewModel()){
    val hotCommunityList = viewModel.hotCommunities.collectAsState()
    val communitiesFollowCategory= viewModel.communitiesFollowCategory.collectAsState()
    val category = viewModel.category.collectAsState()
    val isLoading= viewModel.isLoading.collectAsState()

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

            //community follow category
            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
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
            SectionTitle(title = "Hot Community", modifier = Modifier.padding(start = 20.dp))

            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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
