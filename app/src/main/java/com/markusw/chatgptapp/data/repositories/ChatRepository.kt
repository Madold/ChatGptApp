package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse

interface ChatRepository {
    suspend fun getPromptResponse(prompt: String): Resource<PromptResponse>
}