package com.example.frontend.ui.screen.main_nav


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.components.CommunityCard
import com.example.frontend.ui.components.GerneChipButton
import com.example.frontend.ui.components.ScreenFrame
import com.example.frontend.ui.components.SectionTitle
import com.example.frontend.ui.components.TopBar


@Preview
@Composable
fun CommunityScreen(){
    val categoryList = listOf("Adventure","Fantastic", "Mystery", "Autobiography")
    ScreenFrame(
        topBar = {
            TopBar(
                title = "Community",
                showBackButton = false,
                iconType = "Setting",
                onIconClick = { /*TODO*/ }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            SectionTitle(title = "Hot Community")

            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryList) { item ->
                    GerneChipButton(
                        genre = item,
                        onClick = {}
                    )
                }
            }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .height(height = 168.dp)
            ) {
                items(categoryList) { item ->
                    CommunityCard(
                        item = item
                    )

                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            SectionTitle(title = "Recommended")


            LazyRow(
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .height(height = 168.dp)
            ) {
                items(categoryList) { item ->
                    CommunityCard(
                        item = item
                    )

                }
            }

        }
    }


}
