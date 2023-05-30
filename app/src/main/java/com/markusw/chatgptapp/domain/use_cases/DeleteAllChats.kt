package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.ChatRepository
import javax.inject.Inject

class DeleteAllChats @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.deleteAllChats()
}



