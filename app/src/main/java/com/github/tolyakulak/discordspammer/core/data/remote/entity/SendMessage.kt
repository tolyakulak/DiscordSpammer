package com.github.tolyakulak.discordspammer.core.data.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * used to send a new message
 */
data class SendMessage(
    val content: String,
    @SerializedName("message_reference")
    val reply: MessageReference
)
