package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest


/**
 * Guild item for Lazy column
 * @param iconRequest [ImageRequest] of icon of a guild
 * @param text Text to display. Usually name of the guild
 * @param onClick Block of code to execute when user clicks this item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuildLazyItem(
    iconRequest: ImageRequest,
    text: String,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val widthOfItem = LocalConfiguration.current.screenWidthDp / 3

    Column(
        modifier = modifier
            .width(widthOfItem.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = iconRequest,
            contentDescription = null,
            imageLoader = imageLoader,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(widthOfItem.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
        )
        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}