package com.markusw.chatgptapp.ui.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.markusw.chatgptapp.core.utils.ext.openAppSettings
import com.markusw.chatgptapp.ui.theme.ChatGptAppTheme
import com.markusw.chatgptapp.ui.theme.rememberWindowSizeClass
import com.markusw.chatgptapp.ui.view.screens.main.MainScreen
import com.markusw.chatgptapp.ui.view.screens.main.composables.PermissionDialog
import com.markusw.chatgptapp.ui.view.screens.main.composables.RecordAudioPermissionProvider
import com.markusw.chatgptapp.ui.viewmodel.main.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment: Fragment() {

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
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val windowSizeClass = rememberWindowSizeClass()
                val dialogQueue = viewModel.visiblePermissionDialogQueue
                val context = LocalContext.current
                val focusManager = LocalFocusManager.current
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        viewModel.onPermissionResult(
                            permission = Manifest.permission.RECORD_AUDIO,
                            isGranted = isGranted
                        )
                    }
                )

                ChatGptAppTheme(
                    dynamicColor = false,
                    darkTheme = state.userSettings.darkModeEnabled,
                    windowSizeClass = windowSizeClass
                ) {

                    val systemUiController = rememberSystemUiController()
                    val systemBarsColors = MaterialTheme.colorScheme.background

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = systemBarsColors,
                            darkIcons = !state.userSettings.darkModeEnabled
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        MainScreen(
                            state = state,
                            onSendButtonClick = viewModel::onPromptSend,
                            onPromptChanged = viewModel::onPromptChanged,
                            onThemeChanged = viewModel::onThemeChanged,
                            onNewChat = viewModel::onNewChat,
                            onChatSelected = viewModel::onChatSelected,
                            onPromptCopied = viewModel::onPromptCopied,
                            onDeleteAllChats = viewModel::onDeleteAllChats,
                            onVoiceButtonClick = {
                                if (checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    return@MainScreen
                                }

                                focusManager.clearFocus()
                                viewModel.onVoiceButtonClick()
                            },
                        )

                        dialogQueue
                            .asReversed()
                            .forEach { permission ->
                                PermissionDialog(
                                    permissionTextProvider = when (permission) {
                                        Manifest.permission.RECORD_AUDIO -> {
                                            RecordAudioPermissionProvider()
                                        }

                                        else -> return@forEach
                                    },
                                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                        permission
                                    ),
                                    onDismiss = viewModel::dismissDialog,
                                    onDone = {
                                        viewModel.dismissDialog()
                                        permissionLauncher.launch(permission)
                                    },
                                    onGotToSettings = ::openAppSettings
                                )
                            }

                    }
                }
            }
        }
    }
}