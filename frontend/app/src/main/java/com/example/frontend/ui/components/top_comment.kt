package com.example.frontend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

@Composable
fun TopComments(comments: List<List<Any>>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(comments) { comment ->
            val username = comment[0] as String
            val commentText = comment[1] as? String
            val commentImageRes = comment[2] as? Int
            val chapter = comment[3] as String
            val date = comment[4] as String
            val time = comment[5] as String
            val likes = comment[6] as String
            val unlikes = comment[7] as String

            Card(
                modifier = Modifier
                    .width(335.dp)
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x48828282))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box {
                        //avatar and name
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avt_img),
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .heightIn(50.dp)
                                    .widthIn(50.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color(0xFF4E7AFF),
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(13.dp))
                            Text(
                                text = "@$username",
                                color = Color.White,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (!commentText.isNullOrEmpty() || commentImageRes != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                commentText?.let {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        fontSize = 13.5.sp,
                                        modifier = Modifier.weight(1f).padding(top = 70.dp),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                commentImageRes?.let {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Image(
                                        painter = painterResource(id = it),
                                        contentDescription = "comment image",
                                        modifier = Modifier
                                            .height(110.dp)
                                            .width(90.dp)
                                            .clip(RoundedCornerShape(20.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = chapter, color = Color.White, fontSize = 14.5.sp)
                            Spacer(modifier = Modifier.height(7.dp))
                            Row {
                                Text(text = date, color = Color(0xFFFF5722), fontSize = 14.5.sp)
                                Text(
                                    text = time,
                                    color = Color.White,
                                    fontSize = 14.5.sp,
                                    modifier = Modifier.padding(start = 11.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Icon like and unlike
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.like_icon),
                                contentDescription = "Likes Icon",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(text = likes, color = Color.White, fontSize = 15.sp)

                            Spacer(modifier = Modifier.width(9.dp))

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.unlike_icon),
                                contentDescription = "Unlikes Icon",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(text = unlikes, color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
