package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreenTopBar(
    botStatusText: String,
    isBotTyping: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BasicText(
            text = "Chat-GPT Mobile App",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = botStatusText,
            )
            if (isBotTyping) {
                ThreeDotsAnimation(
                    dotSize = 8.dp,
                    spaceBetweenDots = 4.dp,
                    travelDistance = 4.dp,
                )
            }
        }
    }
}