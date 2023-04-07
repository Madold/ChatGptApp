@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
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
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onPromptChanged,
        trailingIcon = {
            IconButton(onClick = onSendButtonClick) {
                Icon(
                    painterResource(id = R.drawable.ic_send),
                    null
                )
            }
        }
    )
}