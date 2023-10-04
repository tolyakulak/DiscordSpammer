package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api

import com.github.tolyakulak.discordspammer.core.domain.model.User
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException
import com.github.tolyakulak.discordspammer.core.domain.util.rateLimitHandlingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetMyAccountUseCase(
    private val repository: DiscordRepository
) {
    suspend operator fun invoke(token: String): Result<User> =
        kotlin.runCatching {
            if (token.isBlank()) {
                throw InvalidInputException("Token is empty")
            }
            val response = withContext(Dispatchers.IO) {
                rateLimitHandlingRequest { repository.getMe(token) }
            }
            response.body() ?: throw HttpException(response)
        }
}