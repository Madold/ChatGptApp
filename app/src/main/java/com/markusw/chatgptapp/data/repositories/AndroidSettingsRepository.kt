package com.markusw.chatgptapp.data.repositories

import com.markusw.chatgptapp.data.database.daos.UserSettingsDao
import com.markusw.chatgptapp.data.model.UserSettings
import com.markusw.chatgptapp.data.model.toEntityModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AndroidSettingsRepository @Inject constructor(
    private val settingsDao: UserSettingsDao
) : SettingsRepository {

    override fun getUserSettings(): Flow<UserSettings> {
        return settingsDao.getUserSettings()
    }

    override suspend fun saveUserSettings(userSettings: UserSettings) {
        settingsDao.saveUserSettings(userSettings.toEntityModel())
    }
}