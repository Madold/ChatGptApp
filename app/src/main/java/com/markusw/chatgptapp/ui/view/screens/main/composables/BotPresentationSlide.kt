package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.theme.spacing
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState

@Composable
fun BotPresentationSlide(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize()
            .padding(paddingValues)
            .padding(all = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BotExamplesSection()
        BotCapabilitiesSection()
        BotLimitationsSection()
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun BotPresentationSlidePreview() {
    ChatGptAppTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.spacing.small),
                    contentAlignment = Alignment.Center

                ) {
                    PromptField(
                        value = "Example prompt",
                        onPromptChanged = {},
                        onSendButtonClick = {},
                        modifier = Modifier.fillMaxWidth(0.95f)
                    )
                }
            },
            topBar = {
                MainScreenTopBar(
                    state = MainScreenState(),
                )
            },
            content = { padding ->
                BotPresentationSlide(
                    paddingValues = padding
                )
            }
        )
    }
}