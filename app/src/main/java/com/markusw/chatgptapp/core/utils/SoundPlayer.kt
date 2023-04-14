package com.markusw.chatgptapp.core.utils

import android.content.Context
import android.media.MediaPlayer
import com.markusw.chatgptapp.core.common.AppSounds
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun playSound(sound: AppSounds) {
        val mediaPlayer = MediaPlayer.create(context, sound.source)
        mediaPlayer.start()
    }
}

