package com.github.tolyakulak.discordspammer.core.data.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * used to indicate replied message
 * usually returned as message_reference in json
 */
data class MessageReference(
    @SerializedName("message_id") val msgId: String?,
    @SerializedName("channel_id") val channelId: String?,
    @SerializedName("guild_id") val guildId: String?,
)
