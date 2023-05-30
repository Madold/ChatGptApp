package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.domain.ServerEvent
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getPromptResponse(prompts: List<Message>): Resource<Flow<ServerEvent>>
    fun getChatHistory(): Flow<MutableList<ChatHistoryItemEntity>>
    suspend fun deleteAllChats()
    suspend fun saveChat(chat: ChatHistoryItemEntity)
    suspend fun deleteChatById(id: Int)

}