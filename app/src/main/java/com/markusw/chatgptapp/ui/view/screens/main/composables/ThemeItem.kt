@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.R
import com.markusw.chatgptapp.ui.theme.ChatGptAppRippleTheme
import com.markusw.chatgptapp.ui.theme.DarkBlue

@Composable
fun ThemeItem(
    darkModeEnabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalRippleTheme provides ChatGptAppRippleTheme ) {
        NavigationDrawerItem(
            label = {
                Text(
                    text = if (darkModeEnabled) "Light Mode" else "Dark Mode",
                    color = Color.White
                )
            },
            selected = false,
            onClick = onClick,
            icon = {
                Icon(
                    painter = painterResource(id = if (darkModeEnabled) R.drawable.ic_sun else R.drawable.ic_moon),
                    contentDescription = null,
                    tint = Color.White
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = DarkBlue,
            ),
            modifier = Modifier.indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.White)
            )
        )
    }
}