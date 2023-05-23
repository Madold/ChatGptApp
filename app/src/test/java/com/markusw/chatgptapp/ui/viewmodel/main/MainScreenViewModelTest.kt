package com.markusw.chatgptapp.ui.viewmodel.main

import com.markusw.chatgptapp.domain.services.VoiceRecognitionService
import com.markusw.chatgptapp.domain.use_cases.DeleteAllChats
import com.markusw.chatgptapp.domain.use_cases.GetChatHistory
import com.markusw.chatgptapp.domain.use_cases.GetChatResponse
import com.markusw.chatgptapp.domain.use_cases.GetUserSettings
import com.markusw.chatgptapp.domain.use_cases.PlaySound
import com.markusw.chatgptapp.domain.use_cases.SaveChat
import com.markusw.chatgptapp.domain.use_cases.SaveUserSettings
import com.markusw.chatgptapp.domain.use_cases.StartListening
import com.markusw.chatgptapp.domain.use_cases.StopListening
import com.markusw.chatgptapp.domain.use_cases.ValidatePrompt
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before


class MainScreenViewModelTest {

    private lateinit var viewModel: MainScreenViewModel
    @RelaxedMockK
    private lateinit var getChatResponse: GetChatResponse
    @RelaxedMockK
    private lateinit var playSound: PlaySound
    @RelaxedMockK
    private lateinit var validatePrompt: ValidatePrompt
    @RelaxedMockK
    private lateinit var getUserSettings: GetUserSettings
    @RelaxedMockK
    private lateinit var saveUserSettings: SaveUserSettings
    @RelaxedMockK
    private lateinit var getChatHistory: GetChatHistory
    @RelaxedMockK
    private lateinit var saveChat: SaveChat
    @RelaxedMockK
    private lateinit var deleteAllChats: DeleteAllChats
    @RelaxedMockK
    private lateinit var startListening: StartListening
    @RelaxedMockK
    private lateinit var stopListening: StopListening
    @RelaxedMockK
    private lateinit var voiceRecognitionService: VoiceRecognitionService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


}