package com.github.tolyakulak.discordspammer.core.data.repository

import com.github.tolyakulak.discordspammer.core.data.remote.DiscordApi
import com.github.tolyakulak.discordspammer.core.data.remote.entity.MessageReference
import com.github.tolyakulak.discordspammer.core.data.remote.entity.SendMessage
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.model.User
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import retrofit2.Response

class DiscordRepositoryImpl(
    private val api: DiscordApi
) : DiscordRepository {

    override suspend fun getMe(token: String): Response<User> {
        return api.getMe(token)
    }

    override suspend fun getMyGuilds(token: String): Response<List<Guild>> {
        return api.getMyGuilds(token)
    }

    override suspend fun getChannelsOfGuild(token: String, guildId: String): Response<List<Channel>> {
        return api.getChannelsOfGuild(token, guildId)
    }

    override suspend fun sendMessage(
        token: String,
        channelId: String,
        message: String,
        replyToId: String?
    ): Response<Message> {
        return api.createMessage(
            token,
            channelId,
            SendMessage(
                message,
                MessageReference(replyToId, null, null)
            )
        )
    }
}