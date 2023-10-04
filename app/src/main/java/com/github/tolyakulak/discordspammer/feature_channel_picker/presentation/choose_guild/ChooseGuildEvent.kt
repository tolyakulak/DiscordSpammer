package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild

sealed class ChooseGuildEvent {
    /**
     * Event that describes a need to refresh list of guilds
     * @param token authorization token of current user session
     */
    data class UpdateGuilds(val token: String) : ChooseGuildEvent()
}