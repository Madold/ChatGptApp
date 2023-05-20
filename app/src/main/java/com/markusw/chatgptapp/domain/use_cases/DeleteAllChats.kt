package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import javax.inject.Inject

class DeleteAllChats @Inject constructor(
    private val repository: AndroidChatRepository
) {
    suspend operator fun invoke() = repository.deleteAllChats()
}



