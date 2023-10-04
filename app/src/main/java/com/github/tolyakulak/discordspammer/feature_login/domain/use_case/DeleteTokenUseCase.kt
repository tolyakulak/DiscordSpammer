package com.github.tolyakulak.discordspammer.feature_login.domain.use_case

import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import com.github.tolyakulak.discordspammer.feature_login.domain.repository.LocalTokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteTokenUseCase(
    private val repository: LocalTokenRepository
) {

    suspend operator fun invoke(token: Token) {
        withContext(Dispatchers.IO) {
            repository.deleteToken(token)
        }
    }
}