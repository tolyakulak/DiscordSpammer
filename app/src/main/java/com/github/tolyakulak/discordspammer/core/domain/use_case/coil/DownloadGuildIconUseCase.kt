package com.github.tolyakulak.discordspammer.core.domain.use_case.coil

import coil.request.ImageRequest
import com.github.tolyakulak.discordspammer.core.di.CoilModule
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.util.CoilUtils
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException

/**
 * Download icon of a guild
 */
class DownloadGuildIconUseCase(
    private val coilUtils: CoilUtils
) {
    /**
     * @param guild guild you want download icon from
     * @param token authorization token
     * @param size size of image. can be any power of 2 between 16 and 4096
     */
    operator fun invoke(guild: Guild, token: String, size: Int = 512): ImageRequest {
        if(size < 16 || size > 4096) {
            throw InvalidInputException("Size is invalid $size")
        }

        val url = CoilModule.IMAGE_BASE_URL +
                "/icons/${guild.id}/${guild.icon}.jpg?size=$size"

        return coilUtils.requestWithAuthHeader(url, token)
    }
}