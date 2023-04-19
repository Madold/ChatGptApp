package com.markusw.chatgptapp.data.model

import com.markusw.chatgptapp.data.database.entities.UserSettingsEntity

data class UserSettings(
    val id: Int = 0,
    val darkModeEnabled: Boolean = false
)

fun UserSettingsEntity.toDomainModel() = UserSettings(
    id = this.id,
    darkModeEnabled = this.darkModeEnabled
)

fun UserSettings.toEntityModel() = UserSettingsEntity(
    id = this.id,
    darkModeEnabled = this.darkModeEnabled
)