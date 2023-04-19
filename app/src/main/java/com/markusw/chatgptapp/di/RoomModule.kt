package com.markusw.chatgptapp.di

import android.content.Context
import androidx.room.Room
import com.markusw.chatgptapp.core.common.Constants
import com.markusw.chatgptapp.data.database.SettingsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideUserSettingsDb(@ApplicationContext context: Context) = Room
        .databaseBuilder(
            context,
            SettingsDatabase::class.java,
            Constants.USER_SETTINGS_DB
        )
        .build()

    @Provides
    @Singleton
    fun provideSettingsDao(db: SettingsDatabase) = db.getUserSettingsDao()

}