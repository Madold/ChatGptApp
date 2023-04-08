package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.runtime.Composable

@Composable
fun ChatBubble(
    content: String,
) {
    BasicText(text = content)
}