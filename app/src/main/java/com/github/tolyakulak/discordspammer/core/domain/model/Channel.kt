package com.github.tolyakulak.discordspammer.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    val id: String,
    val type: Int,
    @SerializedName("guild_id") val guildId: String? = null,
    val name: String? = null,
    @SerializedName("parent_id")
    val parentId: String? = null
) : Parcelable {
    companion object {
        const val TEXT_CHANNEL = 0
        const val GUILD_CATEGORY = 4
    }
}
