package com.github.tolyakulak.discordspammer.feature_login.domain.use_case

data class LoginUseCases(
    val getTokens: GetTokensUseCase,
    val upsertToken: UpsertTokenUseCase,
    val deleteToken: DeleteTokenUseCase
)