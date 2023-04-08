package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.R

@Composable
fun BotAvatar() {
    Image(
        painter = painterResource(id = R.drawable.chat_gpt_logo),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .size(30.dp)
    )
}