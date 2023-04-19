package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.data.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun saveUserSettings(userSettings: UserSettings)
}