package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.ChatRepository
import javax.inject.Inject

class GetChatHistory @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() = repository.getChatHistory()
}