package com.example.frontend.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.frontend.R
import com.example.frontend.data.model.Comment
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.presentation.viewmodel.story.ReadViewModel
import com.example.frontend.ui.theme.OrangeRed
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TopComments(comments: List<Comment>, viewModel: ReadViewModel) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        itemsIndexed(comments) { index, comment ->

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
                                .clickable {  }
                        ) {
                            AsyncImage(
                                model = comment.user.avatarUrls, // URL của hình ảnh avatar
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color(0xFF4E7AFF),
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(13.dp))
                            Text(
                                text = "@${comment.user?.dName}",
                                color = Color.White,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (!comment.content.isNullOrEmpty() || comment.commentPicUrl != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                comment.content?.let {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        fontSize = 13.5.sp,
                                        modifier = Modifier.weight(1f).padding(top = 70.dp),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                comment.commentPicUrl?.let {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    AsyncImage(
                                        model =  comment.commentPicUrl, // URL của hình ảnh bình luận
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
                            Text(text = comment.chapter.chapterName, color = Color.White, fontSize = 14.5.sp)
                            Spacer(modifier = Modifier.height(7.dp))
                            Row {
                                Text(text = comment.createAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), color = Color(0xFFFF5722), fontSize = 14.5.sp)
                                Text(
                                    text = comment.createAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                                    color = Color.White,
                                    fontSize = 14.5.sp,
                                    modifier = Modifier.padding(start = 11.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Icon like
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.changeLikeState(comment)
                                },
                                modifier = Modifier.size(25.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.like_icon),
                                    contentDescription = "Likes Icon",
                                    tint = if (comment.isUserLike) OrangeRed else Color.White
                                )
                            }
                            Text(text = comment.likeNumber.toString(), color = Color.White, fontSize = 15.sp)

                        }
                    }
                }
            }
        }
    }
}
