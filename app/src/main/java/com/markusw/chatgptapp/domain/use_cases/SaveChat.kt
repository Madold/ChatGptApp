package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.model.ChatHistoryItemModel
import com.markusw.chatgptapp.data.model.toEntity
import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import javax.inject.Inject

class SaveChat @Inject constructor(
    private val repository: AndroidChatRepository
) {
    suspend operator fun invoke(chat: ChatHistoryItemModel) {
        repository.saveChat(chat.toEntity())
    }
}