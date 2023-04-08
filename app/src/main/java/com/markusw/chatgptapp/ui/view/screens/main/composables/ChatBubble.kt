package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole

@Composable
fun ChatBubble(
    chat: ChatMessage,
) {

    val selectionColors = TextSelectionColors(
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
        handleColor = MaterialTheme.colorScheme.tertiary,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if(chat.role == MessageRole.Bot) {
            BotAvatar()
        }
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
            SelectionContainer {
                BasicText(text = chat.content)
            }
        }
    }
}