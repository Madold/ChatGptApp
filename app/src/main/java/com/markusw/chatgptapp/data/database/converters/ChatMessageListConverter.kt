package com.markusw.chatgptapp.data.database.converters

import androidx.room.TypeConverter
import com.markusw.chatgptapp.data.model.ChatMessage
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class ChatMessageListConverter {

    @TypeConverter
    fun fromChatMessageList(chatList: List<ChatMessage>): String = Json.encodeToString(chatList)

    @TypeConverter
    fun toChatMessageList(chatListString: String): List<ChatMessage> = Json.decodeFromString(chatListString)

}