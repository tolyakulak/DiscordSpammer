package com.github.tolyakulak.discordspammer.core.domain.util

import android.app.Application
import coil.request.ImageRequest

class CoilUtils(
    private val appContext: Application
) {
    /**
     * Build a request to image by url and attach authorization header with token
     * @param url url of the image
     * @param token authorization token
     */
    fun requestWithAuthHeader(url: String, token: String): ImageRequest {
        return ImageRequest.Builder(appContext)
            .data(url)
            .setHeader("Authorization", token)
            .build()
    }
}