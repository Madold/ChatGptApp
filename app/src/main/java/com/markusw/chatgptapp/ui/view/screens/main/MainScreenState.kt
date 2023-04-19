package com.markusw.chatgptapp.ui.view.screens.main

import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.UserSettings

data class MainScreenState(
    val prompt: String = "",
    val isPromptValid: Boolean = false,
    val chatList: List<ChatMessage> = listOf(),
    val isBotTyping: Boolean = false,
    val isBotThinking: Boolean = false,
    val botStatusText: String = "Bot is online",
    val userSettings: UserSettings = UserSettings()
)
