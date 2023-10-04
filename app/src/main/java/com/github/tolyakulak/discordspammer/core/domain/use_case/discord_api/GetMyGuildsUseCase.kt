package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api

import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException
import com.github.tolyakulak.discordspammer.core.domain.util.rateLimitHandlingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetMyGuildsUseCase(
    private val repository: DiscordRepository
) {

    suspend operator fun invoke(token: String): Result<List<Guild>> =
        kotlin.runCatching {
            if (token.isBlank()) {
                throw InvalidInputException("Token is empty")
            }
            val response = withContext(Dispatchers.IO) {
                rateLimitHandlingRequest { repository.getMyGuilds(token) }
            }
            response.body() ?: throw HttpException(response)
        }
}