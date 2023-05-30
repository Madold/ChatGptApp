package com.markusw.chatgptapp.di

import com.markusw.chatgptapp.data.database.daos.ChatHistoryDao
import com.markusw.chatgptapp.data.database.daos.UserSettingsDao
import com.markusw.chatgptapp.data.network.services.ChatGptService
import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import com.markusw.chatgptapp.data.repositories.AndroidSettingsRepository
import com.markusw.chatgptapp.data.repositories.ChatRepository
import com.markusw.chatgptapp.data.repositories.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsDao: UserSettingsDao) =
        AndroidSettingsRepository(settingsDao) as SettingsRepository

    @Provides
    @Singleton
    fun provideChatRepository(
        chatGptService: ChatGptService,
        chatHistoryDao: ChatHistoryDao
    ) = AndroidChatRepository(chatGptService, chatHistoryDao) as ChatRepository

}