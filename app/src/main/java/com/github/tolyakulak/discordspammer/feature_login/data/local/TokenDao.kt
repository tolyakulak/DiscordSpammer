package com.github.tolyakulak.discordspammer.feature_login.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {

    @Query("SELECT * FROM token")
    fun getAllTokens(): Flow<List<Token>>

    @Upsert
    suspend fun upsertToken(token: Token)

    @Delete
    suspend fun deleteToken(token: Token)
}