package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel

import android.os.Parcelable
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseChannelState(
    val channelsList: List<Channel> = listOf(),
    val isLoading: Boolean = false
) : Parcelable
