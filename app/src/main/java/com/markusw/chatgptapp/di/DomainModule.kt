package com.markusw.chatgptapp.di

import android.content.Context
import com.markusw.chatgptapp.core.common.FirebaseClient
import com.markusw.chatgptapp.core.utils.AndroidSoundPlayer
import com.markusw.chatgptapp.domain.services.AndroidVoiceRecognitionService
import com.markusw.chatgptapp.domain.services.FirebaseRemoteConfig
import com.markusw.chatgptapp.domain.services.RemoteConfigService
import com.markusw.chatgptapp.domain.services.SoundPlayer
import com.markusw.chatgptapp.domain.services.VoiceRecognitionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideVoiceRecognitionService(@ApplicationContext context: Context) =
        AndroidVoiceRecognitionService(context) as VoiceRecognitionService

    @Provides
    @Singleton
    fun provideSoundPlayerService(@ApplicationContext context: Context) =
        AndroidSoundPlayer(context) as SoundPlayer

    @Provides
    @Singleton
    fun provideRemoteConfigService() = FirebaseRemoteConfig() as RemoteConfigService

}