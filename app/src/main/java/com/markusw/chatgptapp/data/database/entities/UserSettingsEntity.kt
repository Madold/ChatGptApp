package com.markusw.chatgptapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.markusw.chatgptapp.core.common.Constants

@Entity(tableName = Constants.USER_SETTINGS_DB)
data class UserSettingsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val darkModeEnabled: Boolean = false
)
