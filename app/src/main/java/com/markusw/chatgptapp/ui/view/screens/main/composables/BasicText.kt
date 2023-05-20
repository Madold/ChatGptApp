package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.markusw.chatgptapp.ui.theme.Poppins

@Composable
fun BasicText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
        maxLines = maxLines,
        textAlign = textAlign,
        fontFamily = Poppins
    )
}