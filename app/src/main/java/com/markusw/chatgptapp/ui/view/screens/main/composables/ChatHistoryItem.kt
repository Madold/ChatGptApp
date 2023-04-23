package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.markusw.chatgptapp.R
import com.markusw.chatgptapp.data.model.ChatMessage

@Composable
fun ChatHistoryItem(
    chat: List<ChatMessage>,
    index: Int,
    onChatSelected: (Int, List<ChatMessage>) -> Unit,
    selected: Boolean = false
) {
    NavigationDrawerButton(
        label = {
            Text(
                text = chat.firstOrNull()?.content ?: "New Chat",
                color = Color.White
            )
        },
        onClick = {
            onChatSelected(index, chat)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_chat),
                contentDescription = null,
                tint = Color.White
            )
        },
        selected = selected
    )
}