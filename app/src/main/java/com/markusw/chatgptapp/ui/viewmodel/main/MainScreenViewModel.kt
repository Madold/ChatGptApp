package com.markusw.chatgptapp.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.chatgptapp.core.common.AppSounds
import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.data.model.UserSettings
import com.markusw.chatgptapp.data.model.toApiMessage
import com.markusw.chatgptapp.domain.use_cases.GetChatResponse
import com.markusw.chatgptapp.domain.use_cases.GetUserSettings
import com.markusw.chatgptapp.domain.use_cases.PlaySound
import com.markusw.chatgptapp.domain.use_cases.SaveUserSettings
import com.markusw.chatgptapp.domain.use_cases.ValidatePrompt
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getChatResponse: GetChatResponse,
    private val playSound: PlaySound,
    private val validatePrompt: ValidatePrompt,
    private val getUserSettings: GetUserSettings,
    private val saveUserSettings: SaveUserSettings
) : ViewModel() {

    private var _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val userSettings = getUserSettings()
            userSettings.collectLatest { settings ->
                _uiState.update {
                    it.copy(
                        userSettings = settings ?: UserSettings()
                    )
                }
            }
        }
    }

    fun onPromptSend() {
        viewModelScope.launch(Dispatchers.IO) {

            val prompt = _uiState.value.prompt

            _uiState.update {
                it.copy(
                    chatList = it.chatList + ChatMessage(
                        content = prompt,
                        role = MessageRole.User
                    ),
                    prompt = "",
                    botStatusText = "Bot is thinking",
                    isBotThinking = true,
                    isPromptValid = false,
                )
            }

            val prompts = _uiState.value.chatList.map { it.toApiMessage() }

            when (val response = getChatResponse(prompts)) {
                is Resource.Success -> {

                    val responseContent = response.data!!.choices[0].message.content
                    val responseSplit = responseContent.split("\n")
                    Logger.i(responseSplit.toString())

                    playSound(AppSounds.MessageReceived)

                    _uiState.update {
                        it.copy(
                            chatList = it.chatList + ChatMessage(
                                content = responseContent,
                                role = MessageRole.Bot
                            ),
                            botStatusText = "Bot is typing",
                            isBotThinking = false,
                            isBotTyping = true
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            chatList = it.chatList + ChatMessage(
                                content = response.message!!,
                                role = MessageRole.Bot
                            ),
                            botStatusText = "Bot had a problem, try again",
                            isBotThinking = false,
                            isBotTyping = true
                        )
                    }
                }
            }
        }
    }

    fun onPromptChanged(prompt: String) {
        val promptValidationResult = validatePrompt(prompt)
        _uiState.update {
            it.copy(
                prompt = prompt,
                isPromptValid = promptValidationResult.success
            )
        }
    }

    fun onBotTypingFinished() {
        _uiState.update {
            it.copy(
                isBotTyping = false,
                botStatusText = "Bot is online"
            )
        }
    }

    fun onThemeChanged() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSettings = _uiState.value.userSettings
            val newSettings = currentSettings.copy(darkModeEnabled = !currentSettings.darkModeEnabled)
            saveUserSettings(newSettings)
        }
    }
}