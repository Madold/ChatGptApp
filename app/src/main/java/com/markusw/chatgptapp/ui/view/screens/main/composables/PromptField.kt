@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.markusw.chatgptapp.R

@Composable
fun PromptField(
    value: String,
    onPromptChanged: (String) -> Unit,
    onSendButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onPromptChanged,
        trailingIcon = {
            IconButton(onClick = onSendButtonClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        placeholder = {
            BasicText(text = "Send a message...")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            cursorColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}