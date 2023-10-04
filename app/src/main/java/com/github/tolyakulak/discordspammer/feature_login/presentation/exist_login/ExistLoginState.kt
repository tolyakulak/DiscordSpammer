package com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login

import android.os.Parcelable
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExistLoginState(
    val listOfTokens: List<Token> = listOf()
) : Parcelable
