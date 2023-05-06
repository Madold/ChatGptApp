package com.markusw.chatgptapp.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.chatgptapp.core.common.AppSounds
import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.core.utils.ext.collectLatestWithoutSubscribe
import com.markusw.chatgptapp.data.model.ChatHistoryItemModel
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.data.model.UserSettings
import com.markusw.chatgptapp.data.model.addChatMessage
import com.markusw.chatgptapp.data.model.toApiMessage
import com.markusw.chatgptapp.data.model.toDomainModel
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.domain.use_cases.DeleteAllChats
import com.markusw.chatgptapp.domain.use_cases.GetChatHistory
import com.markusw.chatgptapp.domain.use_cases.GetChatResponse
import com.markusw.chatgptapp.domain.use_cases.GetUserSettings
import com.markusw.chatgptapp.domain.use_cases.PlaySound
import com.markusw.chatgptapp.domain.use_cases.SaveChat
import com.markusw.chatgptapp.domain.use_cases.SaveUserSettings
import com.markusw.chatgptapp.domain.use_cases.ValidatePrompt
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState
import com.orhanobut.logger.Logger
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
    private val saveUserSettings: SaveUserSettings,
    private val getChatHistory: GetChatHistory,
    private val saveChat: SaveChat,
    private val deleteAllChats: DeleteAllChats
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

        //Retrieves the locally saved chats
        viewModelScope.launch(Dispatchers.IO) {
            val chatHistory = getChatHistory()
            chatHistory.collectLatest { history ->
                if (history.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(
                            chatHistory = history.map { it.toDomainModel() }.toMutableList(),
                        )
                    }
                }
            }
        }
    }

    fun onPromptSend() {
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = _uiState.value.prompt.trim()
            playSound(AppSounds.MessageSent)

            //Nested functions
            suspend fun handleUserMessage() {

                if (_uiState.value.selectedChatIndex == -1 && _uiState.value.chatHistory.isNotEmpty()) {
                    saveChat(
                        ChatHistoryItemModel(
                            chatList = mutableListOf()
                        )
                    )

                    getChatHistory().collectLatestWithoutSubscribe().also {
                        _uiState.update { state ->
                            state.copy(
                                selectedChatIndex = it.value.lastIndex,
                                selectedChatHistoryItem = it.value.last().toDomainModel()
                            )
                        }
                    }

                }

                if (_uiState.value.selectedChatIndex == -1) {
                    _uiState.update {
                        it.copy(
                            selectedChatIndex = 0,
                            selectedChatHistoryItem = it.selectedChatHistoryItem.copy(
                                id = 1
                            )
                        )
                    }
                }

                _uiState.update {
                    it.copy(
                        selectedChatHistoryItem = it.selectedChatHistoryItem.addChatMessage(
                            ChatMessage(
                                content = prompt,
                                role = MessageRole.User
                            )
                        ),
                        prompt = "",
                        botStatusText = "Bot is thinking",
                        isBotThinking = true,
                        isPromptValid = false,
                    )
                }
            }

            suspend fun handleBotResponse(response: Resource<PromptResponse>) {
                when (response) {
                    is Resource.Success -> {
                        val responseContent = response.data!!.choices[0].message.content
                        _uiState.update {
                            it.copy(
                                selectedChatHistoryItem = _uiState.value.selectedChatHistoryItem.addChatMessage(
                                    ChatMessage(
                                        content = responseContent,
                                        role = MessageRole.Bot
                                    )
                                ),
                                botStatusText = "Bot is typing",
                                isBotThinking = false,
                                isBotTyping = true,
                                wasTypingAnimationPlayed = false
                            )
                        }

                        Logger.d("selectedChatHistoryItem id: ${_uiState.value.selectedChatHistoryItem.id}")
                        saveChat(_uiState.value.selectedChatHistoryItem)
                        playSound(AppSounds.MessageReceived)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                selectedChatHistoryItem = _uiState.value.selectedChatHistoryItem.addChatMessage(
                                    ChatMessage(
                                        content = response.message!!,
                                        role = MessageRole.Bot
                                    )
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

            //Main logic
            handleUserMessage()
            val prompts = _uiState.value.selectedChatHistoryItem.chatList.map { it.toApiMessage() }
            val response = getChatResponse(prompts)
            handleBotResponse(response)
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
            val newSettings = currentSettings.copy(
                darkModeEnabled = !currentSettings.darkModeEnabled
            )
            saveUserSettings(newSettings)
        }
    }

    fun onNewChat() {
        if (_uiState.value.chatHistory.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                saveChat(
                    ChatHistoryItemModel(
                        chatList = listOf()
                    )
                )

                getChatHistory().collectLatestWithoutSubscribe().also {
                    _uiState.update { state ->
                        state.copy(
                            selectedChatIndex = it.value.lastIndex,
                            selectedChatHistoryItem = it.value.last().toDomainModel()
                        )
                    }
                }
            }
        }
    }

    fun onChatSelected(index: Int, chatHistoryItem: ChatHistoryItemModel) {
        _uiState.update {
            it.copy(
                selectedChatHistoryItem = chatHistoryItem,
                selectedChatIndex = index
            )
        }
    }

    fun onPromptCopied() {
        playSound(AppSounds.PromptCopied)
    }

    fun onDeleteAllChats() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllChats()

            getChatHistory().collectLatestWithoutSubscribe().also {
                _uiState.update { state ->
                    state.copy(
                        selectedChatHistoryItem = ChatHistoryItemModel(
                            chatList = listOf()
                        ),
                        selectedChatIndex = -1,
                        chatHistory = it.value.map { it.toDomainModel() }.toMutableList()
                    )
                }
            }
        }
    }

}

