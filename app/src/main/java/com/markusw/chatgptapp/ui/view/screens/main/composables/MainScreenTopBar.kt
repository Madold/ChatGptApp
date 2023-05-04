@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreenTopBar(
    botStatusText: String,
    isBotTyping: Boolean = false,
    isBotThinking: Boolean = false,
    onNavigationIconClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicText(
                    text = "ChatGPT Mobile App",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                            dotSize = 8.dp,
                            spaceBetweenDots = 4.dp,
                            travelDistance = 4.dp,
                        )
                    }
                    AnimatedVisibility(
                        visible = isBotThinking,
                        enter = slideInHorizontally() + expandHorizontally() + fadeIn(
                            animationSpec = tween(delayMillis = 400)
                        ),
                        exit = slideOutHorizontally() + shrinkHorizontally(
                            animationSpec = tween(delayMillis = 400)
                        ) + fadeOut()
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
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