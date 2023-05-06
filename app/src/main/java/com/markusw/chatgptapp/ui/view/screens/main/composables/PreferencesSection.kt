package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState

@Composable
fun PreferencesSection(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onThemeChanged: () -> Unit = {},
    onDeleteAllChats: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Divider(
            color = Color.White.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        ThemeItem(
            darkModeEnabled = state.userSettings.darkModeEnabled,
            onClick = onThemeChanged
        )
        Spacer(modifier = Modifier.height(4.dp))
        DeleteChatsItem(
            onClick = onDeleteAllChats
        )

    }
}