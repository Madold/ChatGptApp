package com.markusw.chatgptapp.ui.view.screens.main.composables

import android.support.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PresentationSlideLabel(
    @DrawableRes iconId: Int,
    text: String,
    iconContentDescription: String? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconContentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )
        BasicText(text = text)
    }
}