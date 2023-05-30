package com.markusw.chatgptapp.domain.services

import com.markusw.chatgptapp.core.common.AppSounds

interface SoundPlayer {
    fun playSound(sound: AppSounds)
}