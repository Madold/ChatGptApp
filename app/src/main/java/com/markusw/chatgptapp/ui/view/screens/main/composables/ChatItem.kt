package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.ui.TestTags
import com.markusw.chatgptapp.ui.theme.spacing

@Composable
fun ChatItem(
    chat: ChatMessage,
    onPromptCopied: () -> Unit = {}
) {

    val isMessageFromBot by remember { derivedStateOf { chat.role == MessageRole.Bot }  }

    Row(
        horizontalArrangement = if (isMessageFromBot) Arrangement.Start else Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isMessageFromBot) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.background,
            )
            .padding(MaterialTheme.spacing.medium)
            .testTag(TestTags.CHAT_ITEM)
    ) {
        ChatBubble(
            chat = chat,
            isFromBot = isMessageFromBot,
            onPromptCopied = onPromptCopied
        )
    }
}