package com.github.tolyakulak.discordspammer.feature_login.domain.use_case

import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import com.github.tolyakulak.discordspammer.feature_login.domain.repository.LocalTokenRepository
import kotlinx.coroutines.flow.Flow

class GetTokensUseCase(
    private val repository: LocalTokenRepository
) {

    operator fun invoke(): Flow<List<Token>> {
        return repository.getAllTokens()
    }
}