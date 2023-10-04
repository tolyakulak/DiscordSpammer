package com.github.tolyakulak.discordspammer.feature_login.presentation.new_login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewLoginState(
    val token: String = "",
    val rememberMe: Boolean = false
): Parcelable