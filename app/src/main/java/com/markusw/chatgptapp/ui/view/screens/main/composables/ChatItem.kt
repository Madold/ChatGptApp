package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole

@Composable
fun ChatItem(
    chat: ChatMessage
) {
    Row(
        horizontalArrangement =
        if (chat.role == MessageRole.Bot) Arrangement.Start
        else Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (chat.role == MessageRole.Bot) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.background,
            )
            .run {
                if (chat.role == MessageRole.Bot) {
                    border(0.5.dp, MaterialTheme.colorScheme.onBackground)
                }
                this
            }
            .padding(16.dp)
    ) {
        ChatBubble(chat = chat)
    }
}