package com.markusw.chatgptapp.ui.viewmodel.main

import androidx.compose.runtime.mutableStateListOf
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
import com.markusw.chatgptapp.domain.ServerEvent
import com.markusw.chatgptapp.domain.services.VoiceRecognitionService
import com.markusw.chatgptapp.domain.use_cases.DeleteAllChats
import com.markusw.chatgptapp.domain.use_cases.FetchApiKey
import com.markusw.chatgptapp.domain.use_cases.GetChatHistory
import com.markusw.chatgptapp.domain.use_cases.GetChatResponse
import com.markusw.chatgptapp.domain.use_cases.GetUserSettings
import com.markusw.chatgptapp.domain.use_cases.PlaySound
import com.markusw.chatgptapp.domain.use_cases.SaveChat
import com.markusw.chatgptapp.domain.use_cases.SaveUserSettings
import com.markusw.chatgptapp.domain.use_cases.StartListening
import com.markusw.chatgptapp.domain.use_cases.StopListening
import com.markusw.chatgptapp.domain.use_cases.ValidatePrompt
import com.markusw.chatgptapp.ui.view.screens.main.MainScreenState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
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
    private val deleteAllChats: DeleteAllChats,
    private val startListening: StartListening,
    private val stopListening: StopListening,
    private val voiceRecognitionService: VoiceRecognitionService,
    private val fetchApiKey: FetchApiKey
) : ViewModel() {

    private var _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()
    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    init {
        //Retrieves the locally saved user settings
        viewModelScope.launch(Dispatchers.IO) {
            getUserSettings()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    UserSettings()
                ).collectLatest { settings ->
                    _uiState.update {
                        it.copy(
                            userSettings = settings ?: UserSettings()
                        )
                    }
                }
        }

        //Retrieves the locally saved chats
        viewModelScope.launch(Dispatchers.IO) {
            getChatHistory()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    listOf()
                )
                .collectLatest { history ->
                    if (history.isNotEmpty()) {
                        _uiState.update { state ->
                            state.copy(
                                chatHistory = history.map { it.toDomainModel() }.toMutableList(),
                            )
                        }
                    }
                }
        }

        //Observing the state of the voice recognition service
        viewModelScope.launch {
            voiceRecognitionService.state.collectLatest { state ->
                _uiState.update { it.copy(isUserSpeaking = state.isSpeaking) }.also {
                    onPromptChanged(state.spokenText)
                }
            }
        }

        // Fetches the API key from the server
        viewModelScope.launch(Dispatchers.IO) { fetchApiKey() }
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
                            chatList = listOf()
                        )
                    )

                    getChatHistory()
                        .collectLatestWithoutSubscribe(scope = viewModelScope)
                        .also {
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
                        isPromptValid = false,
                    )
                }

                _uiState.update { state ->
                    state.copy(
                        selectedChatHistoryItem = _uiState.value.selectedChatHistoryItem.addChatMessage(
                            ChatMessage(
                                content = "",
                                role = MessageRole.Bot
                            )
                        ),
                        isCaretVisible = true
                    )
                }

            }

            fun addChunkToCurrentMessageList(chunk: ServerEvent.BotResponse) {
                var selectedChatList =
                    _uiState.value.selectedChatHistoryItem.chatList
                var lastChatMessage =
                    _uiState.value.selectedChatHistoryItem.chatList.last()
                val lastMessageContent = lastChatMessage.content
                lastChatMessage = lastChatMessage.copy(
                    content = lastMessageContent.plus(chunk.response.choices[0].delta.content)
                )

                selectedChatList = selectedChatList.dropLast(1)

                _uiState.update {
                    it.copy(
                        selectedChatHistoryItem = it.selectedChatHistoryItem.copy(
                            chatList = selectedChatList + lastChatMessage
                        ),
                    )
                }
            }

            suspend fun handleServerEvents(serverEvents: Flow<ServerEvent>) {
                serverEvents.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    ServerEvent.BotStartedTyping
                ).collect { serverEvent ->
                    when (serverEvent) {
                        is ServerEvent.BotStartedTyping -> {
                            _uiState.update {
                                it.copy(
                                    botStatusText = "Bot is typing",
                                    isBotTyping = true,
                                    wasTypingAnimationPlayed = false
                                )
                            }
                        }

                        is ServerEvent.BotResponse -> addChunkToCurrentMessageList(serverEvent)

                        is ServerEvent.BotFinishedTyping -> {
                            _uiState.update {
                                it.copy(
                                    isBotTyping = false,
                                    botStatusText = "Bot is online",
                                    isCaretVisible = false,
                                )
                            }
                            playSound(AppSounds.MessageReceived)
                            saveChat(_uiState.value.selectedChatHistoryItem)
                        }
                    }
                }
            }

            suspend fun handleServerResponse(response: Resource<Flow<ServerEvent>>) {
                when (response) {
                    is Resource.Success -> {
                        handleServerEvents(response.data!!)
                    }

                    is Resource.Error -> {

                        var selectedChatList =
                            _uiState.value.selectedChatHistoryItem.chatList
                        var lastChatMessage =
                            _uiState.value.selectedChatHistoryItem.chatList.last()
                        lastChatMessage = lastChatMessage.copy(
                            content = response.message!!
                        )

                        selectedChatList = selectedChatList.dropLast(1)

                        _uiState.update {
                            it.copy(
                                selectedChatHistoryItem = it.selectedChatHistoryItem.copy(
                                    chatList = selectedChatList + lastChatMessage
                                ),
                                botStatusText = "Bot had a problem, try again",
                                isBotTyping = false,
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
            handleServerResponse(response)
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
                        chatList = mutableListOf()
                    )
                )

                getChatHistory()
                    .collectLatestWithoutSubscribe(scope = viewModelScope)
                    .also {
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

            getChatHistory()
                .collectLatestWithoutSubscribe(scope = viewModelScope)
                .also {
                    _uiState.update { state ->
                        state.copy(
                            selectedChatHistoryItem = ChatHistoryItemModel(
                                chatList = mutableListOf()
                            ),
                            selectedChatIndex = -1,
                            chatHistory = it.value.map { it.toDomainModel() }.toMutableList()
                        )
                    }
                }
        }
    }

    fun onVoiceButtonClick() {
        if (!_uiState.value.isUserSpeaking) {
            startListening()
            return
        }

        stopListening()
    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted) {
            visiblePermissionDialogQueue.add(element = permission)
        }
    }

}

