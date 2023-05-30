package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.SettingsRepository
import javax.inject.Inject

class GetUserSettings @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getUserSettings()
}