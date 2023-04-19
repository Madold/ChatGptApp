package com.markusw.chatgptapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.markusw.chatgptapp.core.common.Constants
import com.markusw.chatgptapp.data.database.entities.UserSettingsEntity
import com.markusw.chatgptapp.data.model.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM ${Constants.USER_SETTINGS_DB} WHERE id = 0")
    fun getUserSettings(): Flow<UserSettings>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSettings(userSettingsEntity: UserSettingsEntity)
}