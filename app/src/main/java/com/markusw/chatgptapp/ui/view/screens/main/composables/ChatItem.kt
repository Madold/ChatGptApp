package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxWidth()
    ) {
        ChatBubble(
            content = chat.content,
            backgroundColor =
            if (chat.role == MessageRole.Bot) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondary
        )
    }
}