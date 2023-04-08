package com.markusw.chatgptapp.core.common

import android.support.annotation.RawRes
import com.markusw.chatgptapp.R

sealed class AppSounds(
    @RawRes val source: Int
) {
    object MessageReceived: AppSounds(source = R.raw.message_received)
}