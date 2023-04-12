package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import kotlinx.coroutines.delay

@Composable
fun ChatBubble(
    chat: ChatMessage,
    isLastMessage: Boolean = false,
    isFromBot: Boolean = false,
    onBotTypingFinished: () -> Unit = {}
) {

    val selectionColors = TextSelectionColors(
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
        handleColor = MaterialTheme.colorScheme.tertiary,
    )
    var textState by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        if (isFromBot && isLastMessage && textState.isEmpty()) {
            chat.content.forEach { char ->
                textState += char
                delay(50)
            }
            delay(1000)
            onBotTypingFinished()
        } else {
            textState = chat.content
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (isFromBot) {
            BotAvatar()
        }
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
            SelectionContainer {
                BasicText(
                    text = textState
                )
            }
        }
    }
}