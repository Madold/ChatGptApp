package com.markusw.chatgptapp.data.model

data class ChatMessage(
    val content: String,
    val role: MessageRole
)

sealed class MessageRole {
    object Bot: MessageRole()
    object User: MessageRole()
}