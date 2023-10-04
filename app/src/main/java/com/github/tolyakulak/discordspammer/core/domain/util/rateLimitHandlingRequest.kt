package com.github.tolyakulak.discordspammer.core.domain.util

import android.util.Log
import kotlinx.coroutines.delay
import retrofit2.Response
import kotlin.math.ceil

/**
 * Rate limit handling request.
 * Waits for rate limits and returns result
 *
 * @param block a network call to retrofit as lambda
 * @return Result of [block]. May be success or not, but never Rate Limit (429)
 */
suspend fun <T> rateLimitHandlingRequest(block: suspend () -> Response<T>): Response<T> {
    while (true) {
        val result = block()

        if(result.code() == 429) {
            val time = result.headers()["Retry-After"]?.toFloatOrNull()
            Log.d("DiscordApi", "RateLimit by discord, waiting $time seconds before trying again")
            delay(time?.let{ ceil(it) }?.toLong() ?: 0L)
        } else {
            return result
        }
    }
}