package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.markusw.chatgptapp.R
import com.markusw.chatgptapp.data.model.ChatHistoryItemModel

@Composable
fun ChatHistoryItem(
    historyItem: ChatHistoryItemModel,
    index: Int,
    onChatSelected: (Int, ChatHistoryItemModel) -> Unit,
    selected: Boolean = false
) {
    NavigationDrawerButton(
        label = {
            Text(
                text = historyItem.chatList.firstOrNull()?.content ?: "New Chat",
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        onClick = {
            onChatSelected(index, historyItem)
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