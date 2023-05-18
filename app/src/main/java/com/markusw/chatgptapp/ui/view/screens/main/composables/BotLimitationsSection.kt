package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.runtime.Composable
import com.markusw.chatgptapp.R

@Composable
fun BotLimitationsSection() {
    Section(
        content = {
            PresentationSlideLabel(
                iconId = R.drawable.ic_warning,
                text = "Limitations"
            )
            PresentationSlideQuote(quote = "May occasionally generate incorrect information")
            PresentationSlideQuote(quote = "May occasionally produce harmful instructions or biased content")
            PresentationSlideQuote(quote = "Limited knowledge of world and events after 2021")
        }
    )
}