package com.markusw.chatgptapp.domain.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.update

class AndroidVoiceRecognitionService(
    private val context: Context
): VoiceRecognitionService(), RecognitionListener {

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    override fun startListening(languageCode: String) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            super._state.update { it.copy(errorMessage = "Speech recognition isn't available ") }
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                languageCode
            )
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        super._state.update { it.copy(isSpeaking = true, spokenText = "") }
    }

    override fun stopListening() {
        recognizer.stopListening()
        super._state.update { it.copy(isSpeaking = false) }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        super._state.update { it.copy(errorMessage = null) }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        super._state.update { it.copy(isSpeaking = false) }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) return
        super._state.update { it.copy(errorMessage = "Error code: $error", isSpeaking = false) }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { spokenText ->
                super._state.update { it.copy(spokenText = spokenText) }
            }
    }
    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit

}

