package com.github.tolyakulak.discordspammer.core.di

import com.github.tolyakulak.discordspammer.core.data.remote.DiscordApi
import com.github.tolyakulak.discordspammer.core.data.repository.DiscordRepositoryImpl
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetChannelsOfGuildUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetMyAccountUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetMyGuildsUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiscordModule {

    @Provides
    @Singleton
    fun provideDiscordApi(): DiscordApi {
        return Retrofit.Builder()
            .baseUrl(DiscordApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DiscordApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDiscordRepository(api: DiscordApi): DiscordRepository {
        return DiscordRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDiscordUseCases(repository: DiscordRepository): DiscordUseCases {
        return DiscordUseCases(
            getChannelsOfGuild = GetChannelsOfGuildUseCase(repository),
            getMyGuilds = GetMyGuildsUseCase(repository),
            getMyAccount = GetMyAccountUseCase(repository),
            sendMessage = SendMessageUseCase(repository)
        )
    }
}