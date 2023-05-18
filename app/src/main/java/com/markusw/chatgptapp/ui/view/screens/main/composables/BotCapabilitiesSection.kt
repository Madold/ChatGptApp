package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.runtime.Composable
import com.markusw.chatgptapp.R

@Composable
fun BotCapabilitiesSection() {
    Section(
        content = {
            PresentationSlideLabel(
                iconId = R.drawable.ic_lightning,
                text = "Capabilities"
            )
            PresentationSlideQuote(quote = "Remembers what user said earlier in the conversation")
            PresentationSlideQuote(quote = "Allows users to provide follow-up corrections")
            PresentationSlideQuote(quote = "Trained to decline inappropriate requests")
        }
    )
}