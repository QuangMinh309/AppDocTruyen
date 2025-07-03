package com.example.frontend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.presentation.viewmodel.story.StoryDetailViewModel
import com.example.frontend.ui.theme.Green
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun StoryStatusAction(
    isAuthor: Boolean,
    storyStatus: MutableState<String>,
    hasVoted: MutableState<String>,
    onActionClick: () -> Unit = {},
    viewModel: StoryDetailViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        modifier = Modifier.padding(horizontal = 9.dp)
    ) {
        // Button: story status
        Button(
            onClick = {
                if (isAuthor) {
                    viewModel.updateStoryStatus()
                    storyStatus.value = if (storyStatus.value == "update") "full" else "update"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (storyStatus.value == "full") Green else OrangeRed
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(35.dp)
                .weight(1f),
            enabled = !viewModel.isLoadingStatus.value // Vô hiệu hóa button khi loading
        ) {
            if (viewModel.isLoadingStatus.value) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = if(storyStatus.value == "full") "Full" else if(storyStatus.value == "update") "Updating" else if(storyStatus.value == "pending") "Pending" else if(storyStatus.value == "approved") "Approved" else "Rejected",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                )
            }
        }

        if (isAuthor) {
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Add chapter",
                    color = Color.Black,
                    fontSize = 15.sp,
                )
            }
        } else {
            val gradientButton = Brush.verticalGradient(
                colors = listOf(Color(0xFFDF4258), Color(0xFF792430))
            )
            val gradientText = Brush.verticalGradient(
                colors = listOf(Color(0xFF792430), Color(0xFFDF4258))
            )

            val isVoted = hasVoted.value == "Voted"
            val background = if (isVoted) Color(0xFFBA3749) else Color.Transparent

            Button(
                onClick = {
                    viewModel.voteStory()
                    // Cập nhật hasVoted dựa trên phản hồi từ viewModel (sẽ được đồng bộ qua LaunchedEffect)
                },
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .then(
                        if (!isVoted) Modifier.drawBehind {
                            val strokeWidth = 3.5.dp.toPx()
                            val halfStroke = strokeWidth / 2

                            drawRoundRect(
                                brush = gradientButton,
                                topLeft = Offset(halfStroke, halfStroke),
                                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                                cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx()),
                                style = Stroke(width = strokeWidth)
                            )
                        } else Modifier
                    )
                    .clip(RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = background,
                    contentColor = if (isVoted) Color.Black else Color.Unspecified
                ),
                contentPadding = PaddingValues(0.dp),
                enabled = !viewModel.isLoadingVote.value // Vô hiệu hóa button khi loading
            ) {
                if (viewModel.isLoadingVote.value) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = if (isVoted) Color.Black else Color(0xFFBA3749),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(35.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.vote_icon),
                            contentDescription = "Vote",
                            modifier = Modifier.size(16.dp),
                            tint = if (isVoted) Color.Black else Color(0xFFBA3749)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = hasVoted.value,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            style = if (!isVoted) {
                                TextStyle(brush = gradientText)
                            } else {
                                TextStyle(color = Color.Black)
                            }
                        )
                    }
                }
            }
        }

        // Add button
        Button(
            onClick = {viewModel.onGoToAddStoyToNameListScreen(viewModel.storyId.value)},
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(35.dp)
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector =Icons.Outlined.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun StoryStatusActionAdmin(
    isAuthor: Boolean,
    storyStatus: MutableState<String>, // "Full" or "Updating"
    hasVoted: MutableState<String>,    // "Vote" or "Voted"
    onActionClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        modifier = Modifier.padding(horizontal = 9.dp)
    ) {
        // Button: story status
        Button(
            onClick = {
                if (isAuthor) {
                    storyStatus.value = when (storyStatus.value) {
                        "full" -> "update"
                        "update" -> "full"
                        else -> storyStatus.value
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (storyStatus.value == "full") Green else OrangeRed
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(35.dp)
                .weight(1f)
        ) {
            Text(
                text = storyStatus.value,
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        if (isAuthor) {
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Edit status",
                    color = Color.Black,
                    fontSize = 15.sp,
                )
            }
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Delete",
                    color = Color.Black,
                    fontSize = 15.sp,
                )
            }
        } else {
            val gradientButton = Brush.verticalGradient(
                colors = listOf(Color(0xFFDF4258), Color(0xFF792430))
            )
            val gradientText = Brush.verticalGradient(
                colors = listOf(Color(0xFF792430), Color(0xFFDF4258))
            )

            val isVoted = hasVoted.value == "voted"
            val background = if (isVoted) Color(0xFFBA3749) else Color.Transparent

            Button(
                onClick = {
                    hasVoted.value = if (isVoted) "vote" else "voted"
                    onActionClick()
                },
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .then(
                        if (!isVoted) Modifier.drawBehind {
                            val strokeWidth = 3.5.dp.toPx()
                            val halfStroke = strokeWidth / 2

                            drawRoundRect(
                                brush = gradientButton,
                                topLeft = Offset(halfStroke, halfStroke),
                                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                                cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx()),
                                style = Stroke(width = strokeWidth)
                            )
                        } else Modifier
                    )
                    .clip(RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = background,
                    contentColor = if (isVoted) Color.Black else Color.Unspecified
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(35.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.vote_icon),
                        contentDescription = "Vote",
                        modifier = Modifier.size(16.dp),
                        tint = if (isVoted) Color.Black else Color(0xFFBA3749)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = hasVoted.value,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        style = if (!isVoted) {
                            TextStyle(brush = gradientText)
                        } else {
                            TextStyle(color = Color.Black)
                        }
                    )
                }
            }
        }
    }
}

