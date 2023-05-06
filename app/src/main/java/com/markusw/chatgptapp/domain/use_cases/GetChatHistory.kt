package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import javax.inject.Inject

class GetChatHistory @Inject constructor(
    private val repository: AndroidChatRepository
) {
    operator fun invoke() = repository.getChatHistory()
}