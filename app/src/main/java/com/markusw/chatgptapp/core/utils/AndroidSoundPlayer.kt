package com.markusw.chatgptapp.core.utils

import android.content.Context
import android.media.MediaPlayer
import com.markusw.chatgptapp.core.common.AppSounds
import com.markusw.chatgptapp.domain.services.SoundPlayer

class AndroidSoundPlayer(
    private val context: Context
) : SoundPlayer {
    override fun playSound(sound: AppSounds) {
        val mediaPlayer = MediaPlayer.create(context, sound.source)
        mediaPlayer.start()
    }
}

