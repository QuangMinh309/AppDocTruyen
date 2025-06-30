package com.example.frontend.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frontend.data.local.dao.ChatDao
import com.example.frontend.data.local.entities.ChatEntity

@Database(
    entities = [ChatEntity::class],
    version = 1
)
abstract class LocalAppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}