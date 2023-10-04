package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api

import androidx.core.text.isDigitsOnly
import com.github.tolyakulak.discordspammer.core.domain.model.Channel
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException
import com.github.tolyakulak.discordspammer.core.domain.util.rateLimitHandlingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetChannelsOfGuildUseCase(
    private val repository: DiscordRepository
) {

    suspend operator fun invoke(token: String, guildId: String): Result<List<Channel>> =
        kotlin.runCatching {
            if (token.isBlank()) {
                throw InvalidInputException("Token is empty")
            }
            if (guildId.isBlank()) {
                throw InvalidInputException("Guild id is empty")
            }
            if (!guildId.isDigitsOnly()) {
                throw InvalidInputException("Guild id must only contain digits")
            }
            val response = withContext(Dispatchers.IO) {
                rateLimitHandlingRequest { repository.getChannelsOfGuild(token, guildId) }
            }
            response.body() ?: throw HttpException(response)
        }

    suspend operator fun invoke(token: String, guild: Guild): Result<List<Channel>> =
        invoke(token, guild.id)
}