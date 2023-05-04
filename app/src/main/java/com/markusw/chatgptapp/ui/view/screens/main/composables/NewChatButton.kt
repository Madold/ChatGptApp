package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NewChatButton(
    onClick: () -> Unit
) {
    NavigationDrawerButton(
        label = { Text(text = "New Chat", color = Color.White)},
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        },
        modifier = Modifier.border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    )
}