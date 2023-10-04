package com.github.tolyakulak.discordspammer.feature_login.data.repository

import com.github.tolyakulak.discordspammer.feature_login.data.local.TokenDao
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import com.github.tolyakulak.discordspammer.feature_login.domain.repository.LocalTokenRepository
import kotlinx.coroutines.flow.Flow

class LocalTokenRepositoryImpl(
    private val tokenDao: TokenDao
) : LocalTokenRepository {

    override fun getAllTokens(): Flow<List<Token>> {
        return tokenDao.getAllTokens()
    }

    override suspend fun upsertToken(token: Token) {
        tokenDao.upsertToken(token)
    }

    override suspend fun deleteToken(token: Token) {
        tokenDao.deleteToken(token)
    }
}