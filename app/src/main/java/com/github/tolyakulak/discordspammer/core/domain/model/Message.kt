package com.github.tolyakulak.discordspammer.core.domain.model

import com.github.tolyakulak.discordspammer.core.data.remote.entity.MessageReference
import com.google.gson.annotations.SerializedName

/**
 * @param timestamp string like `2023-09-28T08:43:07.940000+00:00`
 */
data class Message(
    val id: String,
    @SerializedName("channel_id")
    val channelId: String,
    val author: User,
    val content: String,
    val timestamp: String,
    @SerializedName("edited_timestamp")
    val editedTimestamp: String,
    @SerializedName("message_reference")
    val reply: MessageReference
)
