package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.repositories.ChatRepository
import javax.inject.Inject

class GetChatResponse @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        prompts: List<Message>
    ) = repository.getPromptResponse(prompts)
}