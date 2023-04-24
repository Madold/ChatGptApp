package com.markusw.chatgptapp.core.common

import android.support.annotation.RawRes
import com.markusw.chatgptapp.R

sealed class AppSounds(
    @RawRes val source: Int
) {
    object MessageSent: AppSounds(source = R.raw.message_sent)
    object MessageReceived: AppSounds(source = R.raw.mesage_received)
    object BotTypingFinished: AppSounds(source = R.raw.bot_typing_finished)
    object PromptCopied: AppSounds(source = R.raw.prompt_copied)
}