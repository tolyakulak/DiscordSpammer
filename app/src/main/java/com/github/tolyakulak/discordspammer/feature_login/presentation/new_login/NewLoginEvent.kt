package com.github.tolyakulak.discordspammer.feature_login.presentation.new_login

/**
 * Describes events sent from UI to [NewLoginViewModel]
 */
sealed class NewLoginEvent {
    data class TokenChanged(val value: String): NewLoginEvent()

    data object RememberMeToggled: NewLoginEvent()
    data object Submit: NewLoginEvent()
}
