package com.example.frontend.ui.screen.main_nav


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.presentation.viewmodel.main_nav.CommunityViewModel
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.GerneChipButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar

@Preview(showBackground = true)
@Composable
fun PreviewScreenContent() {
    val fakeViewModel = CommunityViewModel(NavigationManager())
    CommunityScreen(viewModel = fakeViewModel)
}

@Preview
@Composable
fun CommunityScreen(viewModel: CommunityViewModel = hiltViewModel()){
    val hotCommunityList = viewModel.hotCommunityList
    val com = demoCommunity
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
            SectionTitle(title = "Hot Community", modifier = Modifier.padding(start = 20.dp))

            LazyRow(
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(hotCommunityList) { item ->
                    GerneChipButton(
                        genre = item,
                        onClick = {viewModel.filterCommunityFollowCategory(0)}
                    )
                }
            }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
            ) {

                items(hotCommunityList) {
                    CommunityCard(
                        model = com,
                        onClick = {viewModel.onGoToCommunityDetailScreen(com.id)}
                    )

                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            SectionTitle(title = "Recommended", modifier = Modifier.padding(start = 20.dp))


            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
            ) {

                items(hotCommunityList) {
                    CommunityCard(
                        model = com,
                        onClick = {viewModel.onGoToCommunityDetailScreen(com.id)}
                    )

                }
            }

        }
    }


}
