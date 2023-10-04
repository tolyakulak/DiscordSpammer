package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1.5f)
            .padding(
                horizontal = 3.dp,
                vertical = 5.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AsyncImage(
                model = iconRequest,
                contentDescription = null,
                imageLoader = imageLoader,
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black),
                            startY = 50f
                        )
                    )
            )
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = text,
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .offset(x = (-5).dp)
                )
            }
        }
    }
}