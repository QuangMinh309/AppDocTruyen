package com.example.frontend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.theme.OrangeRed

@Composable
fun StoryStatusAction(
    isAuthor: Boolean,
    hasVoted: Boolean = false,
    storyStatus: String, // "Full" or "Updating"
    onActionClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Status of story
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(
                    color = if (storyStatus == "Full") Color(0xFF2ECC71) else Color(0xFFFFC107),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = storyStatus,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Button action (vote or add chapter)
        val text = if (isAuthor) "Add chapter" else if (hasVoted) "Voted" else "Vote"
        val textColor = if (isAuthor) Color.Black else if (hasVoted) Color.LightGray else Color(0xFFFF5376)
//        val icon = if (isAuthor) R.drawable.ic_add_chapter else R.drawable.icon_vote
        val border = if (!isAuthor) BorderStroke(1.dp, textColor) else null
        val bgColor = if (isAuthor) OrangeRed else Color.Transparent

        OutlinedButton(
            onClick = onActionClick,
            modifier = Modifier.height(30.dp),
//            colors = ButtonDefaults.run {
//                outlinedButtonColors(
//                        backgroundColor = bgColor,
//                        contentColor = textColor
//                    )
//            },
            border = border,
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_list_chapter),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = textColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}
