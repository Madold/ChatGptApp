package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.data.repositories.AndroidSettingsRepository
import javax.inject.Inject

class GetUserSettings @Inject constructor(
    private val repository: AndroidSettingsRepository
) {
    operator fun invoke() = repository.getUserSettings()
}