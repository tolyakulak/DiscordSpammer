package com.github.tolyakulak.discordspammer.feature_login.domain.repository

import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface LocalTokenRepository {

    fun getAllTokens(): Flow<List<Token>>
    suspend fun upsertToken(token: Token)
    suspend fun deleteToken(token: Token)
}