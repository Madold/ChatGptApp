package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.domain.services.VoiceRecognitionService
import javax.inject.Inject

class StartListening @Inject constructor(
    private val voiceRecognitionService: VoiceRecognitionService,
    private val getDeviceLanguageCode: GetDeviceLanguageCode
) {
    operator fun invoke() {
        val deviceLanguageCode = getDeviceLanguageCode()
        voiceRecognitionService.startListening(deviceLanguageCode)
    }
}