package com.github.tolyakulak.discordspammer.feature_login.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token

@Database(
    entities = [Token::class],
    version = 1,
    exportSchema = false
)
abstract class TokenDatabase: RoomDatabase() {

    abstract val tokenDao: TokenDao

    companion object {
        const val DATABASE_NAME = "token_db"
    }
}