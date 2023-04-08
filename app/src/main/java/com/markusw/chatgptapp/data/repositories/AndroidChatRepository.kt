package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.data.network.services.ChatGptService
import javax.inject.Inject

class AndroidChatRepository @Inject constructor(
    private val chatGptService: ChatGptService
): ChatRepository {

    override suspend fun getPromptResponse(prompt: String): Resource<PromptResponse> {
        return chatGptService.getPromptResponse(prompt)
    }
}