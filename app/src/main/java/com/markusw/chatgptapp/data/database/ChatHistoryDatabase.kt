package com.markusw.chatgptapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.markusw.chatgptapp.data.database.converters.ChatMessageListConverter
import com.markusw.chatgptapp.data.database.daos.ChatHistoryDao
import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity

@Database(
    entities = [ChatHistoryItemEntity::class],
    version = 1,
)
@TypeConverters(
    ChatMessageListConverter::class
)
abstract class ChatHistoryDatabase : RoomDatabase() {
    abstract fun getChatHistoryDao(): ChatHistoryDao
}