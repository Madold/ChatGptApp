package com.markusw.chatgptapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.markusw.chatgptapp.core.common.Constants.USER_CHATS_DB
import com.markusw.chatgptapp.data.model.ChatMessage

@Entity(tableName = USER_CHATS_DB)
data class ChatHistoryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chatList: List<ChatMessage>,
)
