package com.github.tolyakulak.discordspammer.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Guild(
    val id: String,
    val name: String,
    val icon: String?,
    val description: String?
) : Parcelable
