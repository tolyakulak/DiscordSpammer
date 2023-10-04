package com.github.tolyakulak.discordspammer.core.domain.util

class InvalidInputException(
    message: String = ""
) : Exception("Input is not valid: $message")

