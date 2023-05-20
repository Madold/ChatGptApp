package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.markusw.chatgptapp.R

@Composable
fun DeleteChatsItem(
    onClick: () -> Unit
) {
    NavigationDrawerButton(
        onClick = onClick,
        label = { 
                Text(text = "Clear conversations", color = Color.White)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_trash_can),
                contentDescription = null,
                tint = Color.White
            )
        }
    )
}