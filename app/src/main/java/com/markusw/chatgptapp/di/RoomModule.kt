package com.markusw.chatgptapp.di

import android.content.Context
import androidx.room.Room
import com.markusw.chatgptapp.core.common.Constants
import com.markusw.chatgptapp.core.common.Constants.USER_CHATS_DB
import com.markusw.chatgptapp.data.database.ChatHistoryDatabase
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
        ).build()

    @Provides
    @Singleton
    fun provideSettingsDao(db: SettingsDatabase) = db.getUserSettingsDao()

    @Provides
    @Singleton
    fun provideChatHistoryDb(@ApplicationContext context: Context) = Room
        .databaseBuilder(
            context,
            ChatHistoryDatabase::class.java,
            USER_CHATS_DB
        ).build()

    @Provides
    @Singleton
    fun provideChatHistoryDao(db: ChatHistoryDatabase) = db.getChatHistoryDao()

}