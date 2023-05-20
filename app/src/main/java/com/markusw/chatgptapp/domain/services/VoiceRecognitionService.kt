package com.markusw.chatgptapp.domain.services


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecognitionService @Inject constructor(
    @ApplicationContext private val context: Context
) : RecognitionListener {

    private var _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening(languageCode: String) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update { it.copy(errorMessage = "Speech recognition isn't available ") }
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _state.update { it.copy(isSpeaking = true, spokenText = "") }
    }

    fun stopListening() {
        recognizer.stopListening()
        _state.update { it.copy(isSpeaking = false) }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(errorMessage = null) }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _state.update { it.copy(isSpeaking = false) }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) return
        _state.update { it.copy(errorMessage = "Error code: $error", isSpeaking = false) }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { spokenText ->
                _state.update { it.copy(spokenText = spokenText) }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

}

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val errorMessage: String? = null
)