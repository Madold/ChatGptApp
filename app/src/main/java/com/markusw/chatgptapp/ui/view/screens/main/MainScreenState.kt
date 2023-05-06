package com.markusw.chatgptapp.ui.view.screens.main

import com.markusw.chatgptapp.data.model.ChatHistoryItemModel
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.UserSettings

data class MainScreenState(
    val prompt: String = "",
    val isPromptValid: Boolean = false,
    val chatHistory: MutableList<ChatHistoryItemModel> = mutableListOf(),
    val selectedChatHistoryItem: ChatHistoryItemModel = ChatHistoryItemModel(chatList = listOf()),
    val selectedChatIndex: Int = -1,
    val isBotTyping: Boolean = false,
    val isBotThinking: Boolean = false,
    val botStatusText: String = "Bot is online",
    val userSettings: UserSettings = UserSettings(),
    val wasTypingAnimationPlayed: Boolean = true
)
