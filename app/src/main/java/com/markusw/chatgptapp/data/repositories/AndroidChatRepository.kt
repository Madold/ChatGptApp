package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.database.daos.ChatHistoryDao
import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.data.network.services.ChatGptService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AndroidChatRepository @Inject constructor(
    private val chatGptService: ChatGptService,
    private val chatHistoryDao: ChatHistoryDao
): ChatRepository {

    override suspend fun getPromptResponse(prompts: List<Message>): Resource<PromptResponse> {
        return chatGptService.getPromptResponse(prompts)
    }

    override fun getChatHistory(): Flow<MutableList<ChatHistoryItemEntity>> {
        return chatHistoryDao.getChatHistory()
    }

    override suspend fun deleteAllChats() {
        chatHistoryDao.deleteAllChats()
    }

    override suspend fun saveChat(chat: ChatHistoryItemEntity) {
        chatHistoryDao.saveChat(chat)
    }

    override suspend fun deleteChatById(id: Int) {
        chatHistoryDao.deleteChatById(id)
    }

}