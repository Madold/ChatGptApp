package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.domain.services.VoiceRecognitionService
import javax.inject.Inject

class StopListening @Inject constructor(
    private val voiceRecognitionService: VoiceRecognitionService
) {
    operator fun invoke() {
        voiceRecognitionService.stopListening()
    }
}
