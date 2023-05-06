package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    suspend fun getPromptResponse(prompts: List<Message>): Resource<PromptResponse>
    fun getChatHistory(): Flow<MutableList<ChatHistoryItemEntity>>
    suspend fun deleteAllChats()
    suspend fun saveChat(chat: ChatHistoryItemEntity)
    suspend fun deleteChatById(id: Int)

}