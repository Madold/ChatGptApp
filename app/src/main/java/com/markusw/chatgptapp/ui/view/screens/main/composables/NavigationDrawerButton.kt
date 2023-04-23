@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.ui.theme.DarkBlue
import com.markusw.chatgptapp.ui.theme.DarkBlue50

@Composable
fun NavigationDrawerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit = {},
    selected: Boolean = false
) {
    NavigationDrawerItem(
        label = label,
        selected = selected,
        onClick = onClick,
        icon = icon,
        shape = RoundedCornerShape(8.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = DarkBlue,
            selectedContainerColor = DarkBlue50
        ),
        modifier = modifier.indication(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = Color.White)
        ),
    )
}