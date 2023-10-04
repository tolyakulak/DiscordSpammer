package com.github.tolyakulak.discordspammer.core.presentation.shared_view_model

import android.os.Parcelable
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedVIewModelState(
    val token: String? = null,
    val selectedGuild: Guild? = null,
    val selectedChannel: Channel? = null
) : Parcelable
