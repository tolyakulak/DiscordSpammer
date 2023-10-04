package com.github.tolyakulak.discordspammer.core.domain.use_case.coil

import coil.request.ImageRequest
import com.github.tolyakulak.discordspammer.core.di.CoilModule
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.util.CoilUtils

class DownloadGuildIconUseCase(
    private val coilUtils: CoilUtils
) {

    operator fun invoke(guild: Guild, token: String): ImageRequest {
        val url = CoilModule.IMAGE_BASE_URL +
                "/icons/${guild.id}/${guild.icon}.jpg"

        return coilUtils.requestWithAuthHeader(url, token)
    }
}