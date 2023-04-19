package com.markusw.chatgptapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.markusw.chatgptapp.data.database.daos.UserSettingsDao
import com.markusw.chatgptapp.data.database.entities.UserSettingsEntity

@Database(entities = [UserSettingsEntity::class], version = 1)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun getUserSettingsDao(): UserSettingsDao
}