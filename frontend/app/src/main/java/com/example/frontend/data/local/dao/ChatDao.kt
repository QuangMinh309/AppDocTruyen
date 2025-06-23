package com.example.frontend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.frontend.data.local.entities.ChatEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatEntity)

    // ✅ Hàm mới: chèn nhiều tin nhắn cùng lúc
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<ChatEntity>)

    @Query("SELECT * FROM chats ORDER BY time ASC")
    fun getAllMessages(): Flow<List<ChatEntity>>
}