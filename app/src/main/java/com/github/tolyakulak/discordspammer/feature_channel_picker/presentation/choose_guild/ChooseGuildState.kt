package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild

import android.os.Parcelable
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseGuildState(
    val guildsList: List<Guild> = listOf(),
    val isLoading: Boolean = false
) : Parcelable
