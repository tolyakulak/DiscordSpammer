package com.github.tolyakulak.discordspammer.core.di

import com.github.tolyakulak.discordspammer.core.data.FakeDiscordApi
import com.github.tolyakulak.discordspammer.core.data.remote.DiscordApi
import com.github.tolyakulak.discordspammer.core.data.repository.DiscordRepositoryImpl
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetChannelsOfGuildUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetMyAccountUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.GetMyGuildsUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.SendMessageUseCase
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object TestDiscordModule {
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer().apply {
            dispatcher = FakeDiscordApi
        }
    }

    fun provideDiscordApi(web: MockWebServer): DiscordApi {
        return Retrofit.Builder()
            .baseUrl(web.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DiscordApi::class.java)
    }

    fun provideDiscordRepository(api: DiscordApi): DiscordRepository {
        return DiscordRepositoryImpl(api)
    }

    fun provideDiscordUseCases(repository: DiscordRepository): DiscordUseCases {
        return DiscordUseCases(
            getChannelsOfGuild = GetChannelsOfGuildUseCase(repository),
            getMyGuilds = GetMyGuildsUseCase(repository),
            getMyAccount = GetMyAccountUseCase(repository),
            sendMessage = SendMessageUseCase(repository)
        )
    }
}