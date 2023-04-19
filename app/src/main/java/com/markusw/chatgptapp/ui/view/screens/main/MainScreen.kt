@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.theme.DarkBlue
import com.markusw.chatgptapp.ui.view.screens.main.composables.ChatItem
import com.markusw.chatgptapp.ui.view.screens.main.composables.MainScreenTopBar
import com.markusw.chatgptapp.ui.view.screens.main.composables.PromptField
import com.markusw.chatgptapp.ui.view.screens.main.composables.ThemeItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    state: MainScreenState,
    onSendButtonClick: () -> Unit = {},
    onPromptChanged: (String) -> Unit = {},
    onBotTypingFinished: () -> Unit = {},
    onThemeChanged: () -> Unit = {}
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //Auto scroll to the last item when new message is added
    LaunchedEffect(key1 = state.chatList) {
        if (state.chatList.isNotEmpty()) {
            scrollState.animateScrollToItem(state.chatList.size)
        }
    }

    //Constant scroll to the last item while bot is typing
    LaunchedEffect(key1 = state.isBotTyping) {
        while (state.isBotTyping) {
            delay(50)
            scrollState.animateScrollToItem(state.chatList.size)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ThemeItem(
                            darkModeEnabled = state.userSettings.darkModeEnabled,
                            onClick = onThemeChanged
                        )
                    }
                },
                drawerShape = RectangleShape,
                drawerContainerColor = DarkBlue
            )
        },
        content = {
            Scaffold(
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        PromptField(
                            value = state.prompt,
                            onPromptChanged = onPromptChanged,
                            onSendButtonClick = onSendButtonClick,
                            modifier = Modifier.fillMaxWidth(0.92f),
                            isPromptValid = state.isPromptValid,
                            isEnabled = !state.isBotTyping && !state.isBotThinking
                        )
                    }
                },
                topBar = {
                    MainScreenTopBar(
                        botStatusText = state.botStatusText,
                        isBotTyping = state.isBotTyping,
                        isBotThinking = state.isBotThinking,
                        onNavigationIconClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    )
                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth(),
                        state = scrollState,
                        userScrollEnabled = !state.isBotTyping
                    ) {
                        itemsIndexed(state.chatList) { index, chat ->
                            ChatItem(
                                chat = chat,
                                isLastMessage = index == state.chatList.size - 1,
                                onBotTypingFinished = onBotTypingFinished,
                            )
                        }
                    }
                }
            )
        }
    )
}

@Preview(
    showSystemUi = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    device = Devices.PIXEL_4_XL
)
@Composable
fun MainScreenPreview() {
    ChatGptAppTheme(
        dynamicColor = false,
        darkTheme = false,
    ) {
        MainScreen(
            state = MainScreenState(
                prompt = "Example prompt",
                chatList = listOf(
                    ChatMessage(
                        content = "Hello chat gpt",
                        role = MessageRole.User
                    ),
                    ChatMessage(
                        content = "Hello, how I can help you?",
                        role = MessageRole.Bot
                    ),
                ),
                isBotTyping = false,
                botStatusText = "Bot is typing",
            )
        )
    }
}
