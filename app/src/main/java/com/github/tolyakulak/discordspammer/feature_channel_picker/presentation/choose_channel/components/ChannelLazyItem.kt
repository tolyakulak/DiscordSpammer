package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.domain.model.Channel

/**
 * Channel item for Lazy column
 * @param channel Channel for this item
 * @param onClick block of code to execute when user clicks on this item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelLazyItem(
    channel: Channel, onClick: () -> Unit
) {
    if (channel.type == Channel.TEXT_CHANNEL) {
        Card(
            onClick = onClick, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(
                    horizontal = 2.dp, vertical = 1.dp
                )
        ) {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = channel.name ?: LocalContext.current.getString(R.string.unknown_channel)
                        .format(channel.id),
                    modifier = Modifier.padding(3.dp),
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle
                )
            }
        }
    }

}