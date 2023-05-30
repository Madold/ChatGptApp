@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markusw.chatgptapp.ui.theme.spacing

@Composable
fun MainScreenTopBar(
    botStatusText: String,
    isBotTyping: Boolean = false,
    onNavigationIconClick: () -> Unit = {},
    isNavigationIconButtonEnabled: Boolean = true,
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicText(
                    text = "ChatGPT Mobile App",
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText(
                        text = botStatusText,
                    )
                    AnimatedVisibility(
                        visible = isBotTyping,
                        enter = slideInHorizontally() + expandHorizontally() + fadeIn(
                            animationSpec = tween(delayMillis = 400)
                        ),
                        exit = slideOutHorizontally() + shrinkHorizontally(
                            animationSpec = tween(delayMillis = 400)
                        ) + fadeOut()
                    ) {
                        ThreeDotsAnimation(
                            dotSize = MaterialTheme.spacing.small,
                            spaceBetweenDots = MaterialTheme.spacing.extraSmall,
                            travelDistance = MaterialTheme.spacing.extraSmall,
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick, enabled = isNavigationIconButtonEnabled) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}