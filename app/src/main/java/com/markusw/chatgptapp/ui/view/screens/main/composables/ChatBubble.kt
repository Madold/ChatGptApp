package com.markusw.chatgptapp.ui.view.screens.main.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.R
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import tech.devscion.typist.Typist
import tech.devscion.typist.TypistSpeed

@Composable
fun ChatBubble(
    chat: ChatMessage,
    isLastMessage: Boolean = false,
    isFromBot: Boolean = false,
    wasTypingAnimationPlayed: Boolean = false,
    onBotTypingFinished: () -> Unit = {},
    onPromptCopied: () -> Unit = {}
) {

    val selectionColors = TextSelectionColors(
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
        handleColor = MaterialTheme.colorScheme.tertiary,
    )
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column {
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
        if(isFromBot) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        onPromptCopied()
                        clipboardManager.setText(AnnotatedString(text = chat.content))
                        Toast.makeText(context, "Text copied successfully", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clipboard),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatBubblePreview() {
    ChatGptAppTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ChatBubble(
                chat = ChatMessage(content = "Hi, I'm an AI bot", role = MessageRole.Bot),
                isFromBot = true,
                isLastMessage = false
            )
        }
    }
}