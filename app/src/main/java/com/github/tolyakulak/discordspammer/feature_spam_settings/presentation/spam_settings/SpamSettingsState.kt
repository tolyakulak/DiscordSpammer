package com.github.tolyakulak.discordspammer.feature_spam_settings.presentation.spam_settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpamSettingsState(
    val spamSize: String = ""
) : Parcelable
