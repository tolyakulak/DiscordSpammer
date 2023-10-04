package com.github.tolyakulak.discordspammer.core.presentation.shared_view_model

import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild

sealed class SharedViewModelEvent {
    data class SelectGuild(val guild: Guild): SharedViewModelEvent()
    data class SelectChannel(val channel: Channel): SharedViewModelEvent()
    data class LoginAs(val token: String): SharedViewModelEvent()
}