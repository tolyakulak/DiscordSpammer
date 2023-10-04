package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api


data class DiscordUseCases(
    val getChannelsOfGuild: GetChannelsOfGuildUseCase,
    val getMyAccount: GetMyAccountUseCase,
    val getMyGuilds: GetMyGuildsUseCase,
    val sendMessage: SendMessageUseCase
)