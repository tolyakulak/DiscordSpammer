package com.github.tolyakulak.discordspammer.core.domain.use_case.coil

import coil.request.ImageRequest
import com.github.tolyakulak.discordspammer.core.di.CoilModule
import com.github.tolyakulak.discordspammer.core.domain.model.User
import com.github.tolyakulak.discordspammer.core.domain.util.CoilUtils

class DownloadUserAvatarUseCase(
    private val coilUtils: CoilUtils
) {
    operator fun invoke(user: User, token: String): ImageRequest {
        val url = CoilModule.IMAGE_BASE_URL +
                "/avatars/${user.id}/${user.avatar}.jpg"

        return coilUtils.requestWithAuthHeader(url, token)
    }
}