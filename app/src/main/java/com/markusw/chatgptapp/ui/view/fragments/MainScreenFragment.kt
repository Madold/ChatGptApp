package com.markusw.chatgptapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.view.screens.main.MainScreen
import com.markusw.chatgptapp.ui.viewmodel.main.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var composeView: ComposeView
    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val focusManager = LocalFocusManager.current

                ChatGptAppTheme(
                    dynamicColor = false,
                ) {

                    val systemUiController = rememberSystemUiController()
                    val isSystemInDarkTheme = isSystemInDarkTheme()
                    val systemBarsColors = MaterialTheme.colorScheme.background

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = systemBarsColors,
                            darkIcons = !isSystemInDarkTheme
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen(
                            state = state,
                            onSendButtonClick = {
                                viewModel.onPromptSend()
                                focusManager.clearFocus()
                            },
                            onPromptChanged = viewModel::onPromptChanged
                        )
                    }
                }
            }
        }
    }
}