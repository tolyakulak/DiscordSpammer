package com.github.tolyakulak.discordspammer.feature_login.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Token(
    val token: String,
    val username: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
): Parcelable
