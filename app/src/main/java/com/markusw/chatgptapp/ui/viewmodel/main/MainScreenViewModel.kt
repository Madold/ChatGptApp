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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        //Retrieves the locally saved user settings
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
            val prompt = _uiState.value.prompt.trim()
            val chatHistory = _uiState.value.chatHistory
            playSound(AppSounds.MessageSent)

            // if there is no chat history creates a new chat and add it to the history
            if (chatHistory.isEmpty()) {
                chatHistory.add(_uiState.value.selectedChatList)
                _uiState.update {
                    it.copy(
                        selectedChatList = _uiState.value.selectedChatList + ChatMessage(
                            content = prompt,
                            role = MessageRole.User
                        ),
                        selectedChatIndex = 0,
                        prompt = "",
                        botStatusText = "Bot is thinking",
                        isBotThinking = true,
                        isPromptValid = false,
                        chatHistory = chatHistory
                    )
                }
            } else {
                // if there is a chat history, add the prompt to the current chat
                _uiState.update {
                    it.copy(
                        selectedChatList = _uiState.value.selectedChatList + ChatMessage(
                            content = prompt,
                            role = MessageRole.User
                        ),
                        prompt = "",
                        botStatusText = "Bot is thinking",
                        isBotThinking = true,
                        isPromptValid = false,
                    )
                }
            }
            val prompts = _uiState.value.selectedChatList.map { it.toApiMessage() }

            when (val response = getChatResponse(prompts)) {
                is Resource.Success -> {
                    val responseContent = response.data!!.choices[0].message.content
                    _uiState.update {
                        it.copy(
                            selectedChatList = _uiState.value.selectedChatList + ChatMessage(
                                content = responseContent,
                                role = MessageRole.Bot
                            ),
                            botStatusText = "Bot is typing",
                            isBotThinking = false,
                            isBotTyping = true,
                            chatHistory = chatHistory,
                            wasTypingAnimationPlayed = false
                        )
                    }
                    chatHistory.set(
                        index = _uiState.value.selectedChatIndex,
                        element = _uiState.value.selectedChatList
                    )
                    playSound(AppSounds.MessageReceived)
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            selectedChatList = _uiState.value.selectedChatList + ChatMessage(
                                content = response.message!!,
                                role = MessageRole.Bot
                            ),
                            botStatusText = "Bot had a problem, try again",
                            isBotThinking = false,
                            isBotTyping = true,
                            wasTypingAnimationPlayed = false
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
                botStatusText = "Bot is online",
                wasTypingAnimationPlayed = true
            )
        }
    }

    fun onThemeChanged() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSettings = _uiState.value.userSettings
            val newSettings =
                currentSettings.copy(darkModeEnabled = !currentSettings.darkModeEnabled)
            saveUserSettings(newSettings)
        }
    }

    fun onNewChat() {
        if (_uiState.value.chatHistory.isNotEmpty()) {
            val chatList = _uiState.value.chatHistory
            chatList.add(mutableListOf())
            _uiState.update {
                it.copy(
                    selectedChatList = it.chatHistory.last(),
                    selectedChatIndex = it.chatHistory.lastIndex,
                    chatHistory = chatList,
                    prompt = "",
                )
            }
        }
    }

    fun onChatSelected(index: Int, chatList: List<ChatMessage>) {
        _uiState.update {
            it.copy(
                selectedChatList = chatList,
                selectedChatIndex = index
            )
        }
    }

    fun onPromptCopied() {
        playSound(AppSounds.PromptCopied)
    }

}