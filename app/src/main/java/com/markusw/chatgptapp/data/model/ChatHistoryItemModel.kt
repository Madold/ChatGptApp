package com.markusw.chatgptapp.data.model

import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity
import kotlinx.serialization.Serializable

@Serializable
data class ChatHistoryItemModel(
    val id: Int = 0,
    val chatList: List<ChatMessage>,
)

fun ChatHistoryItemModel.toEntity() = ChatHistoryItemEntity(
    id = id,
    chatList = chatList,
)

fun ChatHistoryItemEntity.toDomainModel() = ChatHistoryItemModel(
    id = id,
    chatList = chatList,
)


fun ChatHistoryItemModel.addChatMessage(chatMessage: ChatMessage) = ChatHistoryItemModel(
    id = id,
    chatList = chatList + chatMessage,
)







