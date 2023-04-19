package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.model.UserSettings
import com.markusw.chatgptapp.data.repositories.AndroidSettingsRepository
import javax.inject.Inject

class SaveUserSettings @Inject constructor(
    private val repository: AndroidSettingsRepository
) {
    suspend operator fun invoke(settings: UserSettings) {
        repository.saveUserSettings(settings)
    }
}