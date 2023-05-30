package com.markusw.chatgptapp.domain.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class VoiceRecognitionService {

    protected var _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()
    abstract fun startListening(languageCode: String)
    abstract fun stopListening()
}

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val errorMessage: String? = null
)