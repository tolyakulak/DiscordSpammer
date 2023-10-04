package com.github.tolyakulak.discordspammer.feature_spam_service.domain.util

import kotlin.random.Random

object ServiceUtils {
    /**
     * Get a string of random characters with given length
     *
     * @param length length of string you want
     * @return string of random characters
     */
    fun getRandomMessage(length: Int = 2000): String =
        Random.nextBytes(length).decodeToString()
}