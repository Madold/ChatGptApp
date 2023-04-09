@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.markusw.chatgptapp.R

@Composable
fun PromptField(
    value: String,
    onPromptChanged: (String) -> Unit,
    onSendButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPromptValid: Boolean = true,
) {

    val sendIconTint by animateColorAsState(
        targetValue = if (isPromptValid) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.4f
        )
    )

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onPromptChanged,
        trailingIcon = {
            IconButton(onClick = onSendButtonClick, enabled = isPromptValid) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null,
                    tint = sendIconTint
                )
            }
        },
        placeholder = {
            BasicText(text = "Send a message...")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            cursorColor = MaterialTheme.colorScheme.onBackground,
        ),
    )
}