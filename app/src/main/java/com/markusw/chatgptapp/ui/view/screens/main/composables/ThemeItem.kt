
package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.markusw.chatgptapp.R

@Composable
fun ThemeItem(
    darkModeEnabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    NavigationDrawerButton(
        label = {
            Text(
                text = if (darkModeEnabled) "Light Mode" else "Dark Mode",
                color = Color.White
            )
        },
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = if (darkModeEnabled) R.drawable.ic_sun else R.drawable.ic_moon),
                contentDescription = null,
                tint = Color.White
            )
        }
    )
}