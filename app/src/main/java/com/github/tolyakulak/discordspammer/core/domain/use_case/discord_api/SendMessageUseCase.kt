package com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api

import androidx.core.text.isDigitsOnly
import com.github.tolyakulak.discordspammer.core.domain.model.Message
import com.github.tolyakulak.discordspammer.core.domain.repository.DiscordRepository
import com.github.tolyakulak.discordspammer.core.domain.util.InvalidInputException
import com.github.tolyakulak.discordspammer.core.domain.util.rateLimitHandlingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SendMessageUseCase(
    private val repository: DiscordRepository
) {

    suspend operator fun invoke(
        token: String, channelId: String, message: String, replyToId: String?
    ): Result<Message> = kotlin.runCatching {
        if (token.isBlank()) {
            throw InvalidInputException("Token is empty")
        }
        if (channelId.isBlank()) {
            throw InvalidInputException("Channel id is empty")
        }
        if (!channelId.isDigitsOnly()) {
            throw InvalidInputException("Channel id must only contain digits")
        }
        if (replyToId?.isBlank() == true) {
            throw InvalidInputException("ReplyToId string cannot be blank. Provide null instead")
        }
        if (replyToId?.isDigitsOnly() == false) {
            throw InvalidInputException("Reply to id must only contain digits")
        }

        val response = withContext(Dispatchers.IO) {
            rateLimitHandlingRequest {
                repository.sendMessage(token, channelId, message, replyToId)
            }
        }
        response.body() ?: throw HttpException(response)
    }
}