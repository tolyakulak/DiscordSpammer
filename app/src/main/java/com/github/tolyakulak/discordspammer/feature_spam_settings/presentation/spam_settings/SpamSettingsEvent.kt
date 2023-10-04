package com.github.tolyakulak.discordspammer.feature_spam_settings.presentation.spam_settings

/**
 * describes events sent from UI to view model
 */
sealed class SpamSettingsEvent {
    data class SpamSizeFieldChanged(val newValue: String): SpamSettingsEvent()
}