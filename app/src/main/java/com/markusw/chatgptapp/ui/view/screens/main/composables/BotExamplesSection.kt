package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.runtime.Composable
import com.markusw.chatgptapp.R

@Composable
fun BotExamplesSection() {
    Section(
        content = {
            PresentationSlideLabel(
                iconId = R.drawable.ic_sun,
                text = "Examples"
            )
            PresentationSlideQuote(quote = "\"Explain quantum in simple terms\"->")
            PresentationSlideQuote(quote = "\"Got any creative ideas for a 10 years old birthday\"->")
            PresentationSlideQuote(quote = "\"How do I make an HTTP request in Javascript\"->")
        }
    )
}