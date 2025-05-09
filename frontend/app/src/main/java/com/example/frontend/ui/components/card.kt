package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
<<<<<<< HEAD
=======
import androidx.compose.foundation.layout.Spacer
>>>>>>> origin/frontend/story-ui
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
<<<<<<< HEAD
import androidx.compose.foundation.layout.wrapContentWidth
=======
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
>>>>>>> origin/frontend/story-ui
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.BlueGray
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun CommunityCard(item:String){
    Column (

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .width(110.dp)
            .background(BlueGray, RoundedCornerShape(10.dp))
            .padding(vertical = 12.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "community avatar",
            modifier = Modifier
                .size(60.dp,60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // Cắt ảnh nếu cần thiết để lấp đầy không gian
        )
        Text(
            text =item,
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding( vertical = 4.dp,horizontal = 10.dp)
        )
        Button(
            onClick = { /*TODO*/ },
            colors =  ButtonDefaults.buttonColors(
                containerColor = OrangeRed
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(20.dp)
        ) {
            Text(
                text =item,
                color = Color.Black,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding( horizontal = 8.dp)
            )

        }
        Text(
            text ="150k menbers",
            color = Color.White,
            style = TextStyle(
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .padding( vertical = 4.dp,horizontal = 10.dp)
        )

    }
}

<<<<<<< HEAD

@Composable
fun MemberCard(item:String){
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.intro_page1_bg),
            contentDescription = "community avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // fill mode
        )
        Column(
            modifier = Modifier
                .padding(start = 15.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Text(
                text = "Peneloped Lynne",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "@$item",
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
        Button(
            onClick = { /*TODO*/ },
            colors =  ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .wrapContentWidth(Alignment.End)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .background(color = OrangeRed, shape = RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Follow",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

=======
@Composable
fun StoryCard(
    coverImage: Painter,
    title: String,
    author: String,
    genres: List<String>,
    lastUpdated: String,
    views: Int,
    chapters: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = coverImage,
            contentDescription = null,
            modifier = Modifier
                .size(99.dp, 152.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.reemkufifun_wght))
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(author, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(13.dp))

            SmallGenreTags(genres)
            Spacer(modifier = Modifier.height(27.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Last Updated: ", color = Color.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(lastUpdated, color = OrangeRed, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(11.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                    contentDescription = "View Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text("$views", color = Color.White, fontSize = 12.5.sp)

                Spacer(modifier = Modifier.width(25.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.list_chapter_icon),
                    contentDescription = "List Chapter Icon",
                    tint = OrangeRed,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text("$chapters", color = Color.White, fontSize = 12.5.sp)
            }
        }
    }
    Spacer(Modifier.height(11.dp))
}

@Composable
fun SimilarNovelsCard(novels: List<List<Any>>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(17.dp)) {
        items(novels, key = { it[1].toString() }) { novel ->
            val imageRes = novel[0] as Int
            val title = novel[1] as String
            val author = novel[2] as String
            val price = novel[3] as String
            val votes = novel[4] as String

            Column(
                modifier = Modifier.width(128.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .height(184.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(11.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = author,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.price_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = OrangeRed
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = price,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.popular_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = OrangeRed
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = votes,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
>>>>>>> origin/frontend/story-ui
        }
    }
}

@Composable
<<<<<<< HEAD
fun NotificationCard(cardType :String ,
                    transactionContent:String = "",
                    transactionType: String = "",
                    time: String = ""
                    ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ){
        if(cardType == "transactionNotification")
            Icon(
                imageVector = when (transactionType) {
                    "Withdraw" -> Icons.Filled.LocalAtm
                    "Recharge" -> Icons.Filled.AccountBalance
                    "Transfer" -> Icons.Filled.Payments
                    else -> Icons.Filled.QuestionMark
                },
                contentDescription = "transaction icon" ,
                tint = Color.White,
                modifier = Modifier.size(40.dp).padding(horizontal = 5.dp)
            )
        else{
            Image(
                painter = painterResource(id = R.drawable.intro_page1_bg),
                contentDescription =null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop // fill mode
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 15.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){

            Text(
                text = transactionContent,
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Text(
                text = time,
                color = Color.LightGray,
                style = TextStyle(
                    fontSize = 12.sp
                )
            )
=======
fun ChapterItemCard(
    title: String,
    date: String,
    time: String,
    commentCount: String,
    viewCount: String,
    isLocked: Boolean = false,
    isAuthor: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Text(text = title, color = Color.White, fontSize = 19.sp)
                Spacer(modifier = Modifier.width(11.dp))
                if (isAuthor) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.write_icon),
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = Color.White
                    )
                } else if (isLocked) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.lock_icon),
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = OrangeRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(13.dp))

            Row {
                Text(text = date, color = OrangeRed, fontSize = 14.sp)
                Text(
                    text = time,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.comment_icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text("$commentCount", color = Color.White, fontSize = 15.sp)

                    Spacer(modifier = Modifier.width(11.dp))

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.view_icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text("$viewCount", color = Color.White, fontSize = 15.sp)
                }
            }
>>>>>>> origin/frontend/story-ui
        }
    }
}
