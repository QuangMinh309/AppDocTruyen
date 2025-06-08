package com.example.frontend.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")

data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val chatId: Int = 0,
    val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String?,
    val communityId: Int,
    val content: String?,
    val time: String,
    val messagePicUrl: String?
)
