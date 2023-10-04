package com.github.tolyakulak.discordspammer.core.di

import android.app.Application
import coil.ImageLoader
import coil.decode.GifDecoder
import com.github.tolyakulak.discordspammer.core.domain.use_case.coil.CoilUseCases
import com.github.tolyakulak.discordspammer.core.domain.use_case.coil.DownloadGuildIconUseCase
import com.github.tolyakulak.discordspammer.core.domain.use_case.coil.DownloadUserAvatarUseCase
import com.github.tolyakulak.discordspammer.core.domain.util.CoilUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    const val IMAGE_BASE_URL = "https://cdn.discordapp.com"

    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader
            .Builder(app)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }


    @Provides
    @Singleton
    fun provideCoilUtils(app: Application): CoilUtils {
        return CoilUtils(app)
    }

    @Provides
    @Singleton
    fun provideCoilUseCases(coilUtils: CoilUtils): CoilUseCases {
        return CoilUseCases(
            downloadGuildIcon = DownloadGuildIconUseCase(coilUtils),
            downloadUserAvatar = DownloadUserAvatarUseCase(coilUtils),
        )
    }
}