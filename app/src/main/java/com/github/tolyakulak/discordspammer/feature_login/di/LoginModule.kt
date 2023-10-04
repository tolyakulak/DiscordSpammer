package com.github.tolyakulak.discordspammer.feature_login.di

import android.app.Application
import com.github.tolyakulak.discordspammer.feature_login.data.local.TokenDatabase
import com.github.tolyakulak.discordspammer.feature_login.data.repository.LocalTokenRepositoryImpl
import com.github.tolyakulak.discordspammer.feature_login.domain.repository.LocalTokenRepository
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.DeleteTokenUseCase
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.GetTokensUseCase
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.LoginUseCases
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.UpsertTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LoginModule {
    @Provides
    @ViewModelScoped
    fun provideDatabase(app: Application): TokenDatabase {
        return androidx.room.Room.databaseBuilder(
            app,
            TokenDatabase::class.java,
            TokenDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @ViewModelScoped
    fun provideRepository(db: TokenDatabase): LocalTokenRepository {
        return LocalTokenRepositoryImpl(db.tokenDao)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCases(repo: LocalTokenRepository): LoginUseCases {
        return LoginUseCases(
            getTokens = GetTokensUseCase(repo),
            upsertToken = UpsertTokenUseCase(repo),
            deleteToken = DeleteTokenUseCase(repo)
        )
    }
}