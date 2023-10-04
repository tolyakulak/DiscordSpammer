package com.github.tolyakulak.discordspammer.core.domain.repository

import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.model.User
import retrofit2.Response

interface DiscordRepository {
    suspend fun getMe(token: String): Response<User>
    suspend fun getMyGuilds(token: String): Response<List<Guild>>
    suspend fun getChannelsOfGuild(token: String, guildId: String): Response<List<Channel>>
    suspend fun sendMessage(
        token: String,
        channelId: String,
        message: String,
        replyToId: String?
    ): Response<Message>

}