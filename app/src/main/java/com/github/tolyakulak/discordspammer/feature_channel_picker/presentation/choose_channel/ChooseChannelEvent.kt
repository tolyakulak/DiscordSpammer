package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel

import com.github.tolyakulak.discordspammer.core.domain.model.Guild

sealed class ChooseChannelEvent {
    /**
     * Event that describes a need to refresh list of channels
     *
     * @param token authorization token of current user session
     * @param guild guild you want to get channels from
     */
    data class UpdateChannels(
        val token: String,
        val guild: Guild
    ) : ChooseChannelEvent()
}