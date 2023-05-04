package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.ui.theme.ChatGptAppRippleTheme
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState

@Composable
fun NavigationDrawerContent(
    state: MainScreenState,
    onThemeChanged: () -> Unit = {},
    onNewChat: () -> Unit = {},
    onChatSelected: (Int, List<ChatMessage>) -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides ChatGptAppRippleTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            NewChatButton(onClick = onNewChat)
            ChatHistoryList(
                state = state,
                modifier = Modifier.weight(1f).padding(top = 8.dp),
                onChatSelected = onChatSelected
            )
            PreferencesSection(
                state = state,
                onThemeChanged = onThemeChanged,
            )
        }
    }
}