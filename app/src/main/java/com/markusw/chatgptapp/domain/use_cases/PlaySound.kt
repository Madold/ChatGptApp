package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.core.common.AppSounds
import com.markusw.chatgptapp.domain.services.SoundPlayer
import javax.inject.Inject

class PlaySound @Inject constructor(
    private val soundPlayer: SoundPlayer
) {
    operator fun invoke(sound: AppSounds) {
        soundPlayer.playSound(sound)
    }
}