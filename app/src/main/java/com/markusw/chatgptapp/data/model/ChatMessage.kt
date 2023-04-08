package com.markusw.chatgptapp.data.model

import com.markusw.chatgptapp.data.network.remote.responses.Message

data class ChatMessage(
    val content: String,
    val role: MessageRole
)

sealed class MessageRole {
    object Bot: MessageRole()
    object User: MessageRole()
}

fun ChatMessage.toApiMessage(): Message {
    val role = if(this.role == MessageRole.Bot) "assistant" else "user"

    return Message(
        content = content,
        role = role
    )
}