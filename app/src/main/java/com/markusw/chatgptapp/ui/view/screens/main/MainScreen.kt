@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.chatgptapp.ui.view.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.view.screens.main.composables.ChatItem
import com.markusw.chatgptapp.ui.view.screens.main.composables.MainScreenTopBar
import com.markusw.chatgptapp.ui.view.screens.main.composables.PromptField
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    state: MainScreenState,
    onSendButtonClick: () -> Unit = {},
    onPromptChanged: (String) -> Unit = {},
    onBotTypingFinished: () -> Unit = {}
) {

    val scrollState = rememberLazyListState()

    //Auto scroll to the last item when new message is added
    LaunchedEffect(key1 = state.chatList) {
        if (state.chatList.isNotEmpty()) {
            scrollState.animateScrollToItem(state.chatList.size)
            Logger.d("Scrolling to last item")
        }
    }

    //Constant scroll to the last item while bot is typing
    LaunchedEffect(key1 = state.isBotTyping) {
        while (state.isBotTyping) {
            delay(50)
            scrollState.animateScrollToItem(state.chatList.size)
        }
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
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
                isBotThinking = state.isBotThinking
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(),
                state = scrollState
            ) {
                itemsIndexed(state.chatList) { index, chat ->
                    ChatItem(
                        chat = chat,
                        isLastMessage = index == state.chatList.size - 1,
                        onBotTypingFinished = onBotTypingFinished
                    )
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    ChatGptAppTheme {
        MainScreen(state = MainScreenState())
    }
}