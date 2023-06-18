package com.markusw.chatgptapp.ui.view.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
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
import com.markusw.chatgptapp.data.model.ChatHistoryItemModel
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.theme.DarkBlue
import com.markusw.chatgptapp.ui.theme.rememberWindowSizeClass
import com.markusw.chatgptapp.ui.theme.spacing
import com.markusw.chatgptapp.ui.view.screens.main.composables.BotPresentationSlide
import com.markusw.chatgptapp.ui.view.screens.main.composables.ChatItem
import com.markusw.chatgptapp.ui.view.screens.main.composables.MainScreenTopBar
import com.markusw.chatgptapp.ui.view.screens.main.composables.NavigationDrawerContent
import com.markusw.chatgptapp.ui.view.screens.main.composables.PromptField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    state: MainScreenState,
    onSendButtonClick: () -> Unit = {},
    onPromptChanged: (String) -> Unit = {},
    onThemeChanged: () -> Unit = {},
    onNewChat: () -> Unit = {},
    onChatSelected: (Int, ChatHistoryItemModel) -> Unit = { _, _ -> },
    onPromptCopied: () -> Unit = {},
    onDeleteAllChats: () -> Unit = {},
    onVoiceButtonClick: () -> Unit = {}
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //Auto scroll to the last item when new message is added
    LaunchedEffect(key1 = state.selectedChatHistoryItem) {
        if (state.selectedChatHistoryItem.chatList.isNotEmpty()) {
            scrollState.animateScrollToItem(state.selectedChatHistoryItem.chatList.size)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !state.isBotTyping,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    NavigationDrawerContent(
                        state = state,
                        onThemeChanged = {
                            onThemeChanged()
                            coroutineScope.launch {
                                delay(200)
                                drawerState.close()
                            }
                        },
                        onNewChat = {
                            onNewChat()
                            coroutineScope.launch {
                                delay(200)
                                drawerState.close()
                            }
                        },
                        onChatSelected = { index, chat ->
                            onChatSelected(index, chat)
                            coroutineScope.launch {
                                delay(200)
                                drawerState.close()
                            }
                        },
                        onDeleteAllChats = {
                            onDeleteAllChats()
                            coroutineScope.launch {
                                delay(200)
                                drawerState.close()
                            }
                        }
                    )
                },
                drawerShape = RectangleShape,
                drawerContainerColor = DarkBlue,
            )
        },
        content = {
            Scaffold(
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spacing.small),
                        contentAlignment = Alignment.Center

                    ) {
                        PromptField(
                            value = state.prompt,
                            onPromptChanged = onPromptChanged,
                            onSendButtonClick = onSendButtonClick,
                            modifier = Modifier.fillMaxWidth(0.92f),
                            isSendButtonEnabled = state.isPromptValid && !state.isBotTyping,
                            isSpeaking = state.isUserSpeaking,
                            onVoiceButtonClick = onVoiceButtonClick
                        )
                    }
                },
                topBar = {
                    MainScreenTopBar(
                        state = state,
                        onNavigationIconClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    )
                },
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        if (state.selectedChatHistoryItem.chatList.isEmpty()) {
                            BotPresentationSlide(modifier = Modifier.fillMaxSize())
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                state = scrollState,
                                userScrollEnabled = !state.isBotTyping
                            ) {
                                itemsIndexed(state.selectedChatHistoryItem.chatList) { index, chat ->
                                    ChatItem(
                                        isCaretVisible = state.isCaretVisible,
                                        chat = chat,
                                        onPromptCopied = onPromptCopied,
                                        isLast = index == state.selectedChatHistoryItem.chatList.size - 1
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun MainScreenPreview() {
    ChatGptAppTheme(
        dynamicColor = false,
        darkTheme = true,
        windowSizeClass = rememberWindowSizeClass()
    ) {
        MainScreen(
            state = MainScreenState(
                prompt = "Example prompt",
                isBotTyping = false,
                botStatusText = "Bot is typing",
            )
        )
    }
}