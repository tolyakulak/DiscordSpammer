package com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login

import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token

/**
 * Events sent from UI to [ExistLoginViewModel]
 */
sealed class ExistLoginEvent {
    data class DeleteToken(val token: Token): ExistLoginEvent()
}
