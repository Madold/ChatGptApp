package com.markusw.chatgptapp.data.model

import com.markusw.chatgptapp.data.network.remote.responses.Message
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val content: String,
    val role: MessageRole
)

@Serializable
sealed interface MessageRole {
    @Serializable
    object Bot : MessageRole
    @Serializable
    object User : MessageRole
}

fun ChatMessage.toApiMessage(): Message {
    val role = if (this.role == MessageRole.Bot) "assistant" else "user"

    return Message(
        content = content,
        role = role
    )
}