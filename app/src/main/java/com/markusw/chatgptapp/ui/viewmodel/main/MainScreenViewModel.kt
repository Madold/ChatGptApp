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

/*
* ViewModel for the main screen
*
* @property getChatResponse: Use case for retrieving the chat response from the server
* @property Use case for playing a sound
* @property Use case for validating the prompt from the user
* @property Use case for retrieving the user settings from the local database
* @property Use case for saving the user settings to the local database
* @property Use case for retrieving the chat history from the local database
* @property Use case for saving the chat history to the local database
* @property Use case for deleting all chats from the local database
* @property Use case for starting the voice recognition service
* @property Use case for stopping the voice recognition service
* @property Service for voice recognition
* @property Use case for fetching the API key from the remote server
* */
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

    /*
    * Mutable state flow for the UI state
    * */
    private var _uiState = MutableStateFlow(MainScreenState())
    /*
    * Exposed state flow for the UI state
    * */
    val uiState = _uiState.asStateFlow()
    /*
    * Queue for the permission dialog
    * */
    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    /*
    * initial method for retrieving the user settings, chat history and fetching the API key
    * */
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

    /*
    * Method for handling the user prompt
    * */
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
                                wasTypingAnimationPlayed = false,
                                isCaretVisible = false
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


    /*
    * Method to update and validate the prompt state
    * @param prompt
    * */
    fun onPromptChanged(prompt: String) {
        val promptValidationResult = validatePrompt(prompt)
        _uiState.update {
            it.copy(
                prompt = prompt,
                isPromptValid = promptValidationResult.success
            )
        }
    }

    /*
    * Method to handle the theme change
    * */
    fun onThemeChanged() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSettings = _uiState.value.userSettings
            val newSettings = currentSettings.copy(
                darkModeEnabled = !currentSettings.darkModeEnabled
            )
            saveUserSettings(newSettings)
        }
    }

    /*
    * Method to handle when user creates a new chat
    * */
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

    /*
    * Method to handle when user selects a chat from the chat history
    * @param index
    * @param chatHistoryItem
    * */
    fun onChatSelected(index: Int, chatHistoryItem: ChatHistoryItemModel) {
        _uiState.update {
            it.copy(
                selectedChatHistoryItem = chatHistoryItem,
                selectedChatIndex = index
            )
        }
    }

    /*
    * Plays a sound when user copies a prompt from bot
    * */
    fun onPromptCopied() {
        playSound(AppSounds.PromptCopied)
    }

    /*
    * Deletes all chats from the chat history
    * */
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

    /*
    * Handles when user clicks on the voice button
    *
    * when user is not speaking, it starts listening else it stops listening
    * */
    fun onVoiceButtonClick() {
        if (!_uiState.value.isUserSpeaking) {
            startListening()
            return
        }

        stopListening()
    }

    /*
    * Dismiss the current dialog from the permission dialog queue
    * */
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    /*
    * Handles the permission result
    * @param permission
    * @param isGranted
    * */
    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted) {
            visiblePermissionDialogQueue.add(element = permission)
        }
    }

}

