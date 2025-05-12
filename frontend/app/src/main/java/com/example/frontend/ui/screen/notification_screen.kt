package com.example.frontend.ui.screen


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.components.NotificationCard
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.TopBar


@Preview
@Composable
fun NotificationScreen(){
    val historyList = listOf("Bạn đã nạp 300.000₫ vào tài .","Bạn đã chi 300.000₫ để mua “Tempting the divine”.","tác giả @tolapenelope đã đăng tải chapter 3 cuốn tiểu thuyết “Tempting the divine” .")
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Notification",
                showBackButton = true,
                iconType = "Setting",
                onLeftClick = { /*TODO*/ },
                onRightClick = { /*TODO*/ }
            )
        }
    ){
        LazyColumn (
            modifier = Modifier
                .padding(vertical = 40.dp)
        ){
            items(historyList.size) { index ->
                NotificationCard(
                    cardType = if(index!=2) "transactionNotification" else "SocialNotification",
                    transactionContent = historyList[index],
                    transactionType = if(index==0) "Recharge" else "Transfer",
                    time = "2 day ago"
                )
            }

        }
    }


}
