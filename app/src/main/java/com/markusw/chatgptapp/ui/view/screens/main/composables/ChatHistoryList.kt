package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState

@Composable
fun ChatHistoryList(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onChatSelected: (Int, List<ChatMessage>) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(state.chatHistory) { index, chat ->
            ChatHistoryItem(
                chat = chat,
                index = index,
                onChatSelected = onChatSelected,
                selected = index == state.selectedChatIndex
            )
        }
    }
}