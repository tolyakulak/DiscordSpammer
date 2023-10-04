package com.github.tolyakulak.discordspammer.core.data.remote

import com.github.tolyakulak.discordspammer.core.data.remote.entity.SendMessage
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit interface for Discord Rest API
 */
interface DiscordApi {

    @GET("users/@me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<User>

    @GET("users/@me/guilds")
    suspend fun getMyGuilds(
        @Header("Authorization") token: String
    ): Response<List<Guild>>

    @GET("guilds/{guild_id}/channels")
    suspend fun getChannelsOfGuild(
        @Header("Authorization") token: String,
        @Path("guild_id") guildId: String
    ): Response<List<Channel>>

    @POST("channels/{channel_id}/messages")
    suspend fun createMessage(
        @Header("Authorization") token: String,
        @Path("channel_id") channelId: String,
        @Body message: SendMessage
    ): Response<Message>

    companion object {
        const val BASE_URL = "https://discord.com/api/v10/"
    }
}