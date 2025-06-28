package com.example.frontend.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.presentation.viewmodel.NotificationViewModel
import com.example.frontend.presentation.viewmodel.main_nav.ProfileViewModel
import com.example.frontend.services.navigation.NavigationManager
import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar
import com.example.frontend.ui.screen.main_nav.ProfileScreen
import java.time.LocalDate
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
fun PreviewScreenContent2() {
    val fakeViewModel = NotificationViewModel(NavigationManager())
    NotificationScreen(viewModel = fakeViewModel)
}


@Composable
fun NotificationScreen(viewModel: NotificationViewModel= hiltViewModel()){
    val historyList = listOf("Bạn đã nạp 300.000₫ vào tài .","Bạn đã chi 300.000₫ để mua “Tempting the divine”.","tác giả @tolapenelope đã đăng tải chapter 3 cuốn tiểu thuyết “Tempting the divine” .")
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Notification",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { viewModel.onGoBack() },
                onRightClick = { viewModel.onGoToSetting() }
            )
        }
    ){
        LazyColumn (
            modifier = Modifier
                .padding(vertical = 40.dp)
        ){
            items(historyList.size) { index ->
                Box{
                    NotificationCard(
                        content = historyList[index],
                        type = if(index==0) "Recharge" else "Transfer",
                        time = LocalDateTime.now()
                    )
                }
            }

        }
    }


}
