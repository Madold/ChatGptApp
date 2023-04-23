package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import tech.devscion.typist.Typist
import tech.devscion.typist.TypistSpeed

@Composable
fun ChatBubble(
    chat: ChatMessage,
    isLastMessage: Boolean = false,
    isFromBot: Boolean = false,
    wasTypingAnimationPlayed: Boolean = false,
    onBotTypingFinished: () -> Unit = {}
) {

    val selectionColors = TextSelectionColors(
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
        handleColor = MaterialTheme.colorScheme.tertiary,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (isFromBot) {
            BotAvatar()
        }
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
            SelectionContainer {
                if (isFromBot && isLastMessage && !wasTypingAnimationPlayed) {
                    Typist(
                        text = chat.content,
                        onAnimationEnd = onBotTypingFinished,
                        cursorColor = MaterialTheme.colorScheme.onBackground,
                        isBlinkingCursor = true,
                        isInfiniteCursor = false,
                        typistSpeed = TypistSpeed.EXTRA_FAST,
                        isCursorVisible = false,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                } else {
                    BasicText(text = chat.content)
                }
            }
        }
    }
}