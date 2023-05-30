package com.markusw.chatgptapp.domain

import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse

sealed interface ServerEvent {
    data class BotResponse(val response: PromptResponse): ServerEvent
    object BotStartedTyping: ServerEvent
    object BotFinishedTyping: ServerEvent
}
