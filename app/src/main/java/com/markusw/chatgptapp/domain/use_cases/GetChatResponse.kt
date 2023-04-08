package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import javax.inject.Inject

class GetChatResponse @Inject constructor(
    private val repository: AndroidChatRepository
) {
    suspend operator fun invoke(prompts: List<Message>): Resource<PromptResponse> {
        return repository.getPromptResponse(prompts)
    }
}