package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.github.tolyakulak.discordspammer.R

/**
 * Infinite loading animation
 *
 * Fills max size of parent component
 */
@Composable
fun LoadingAnimation(imageLoader: ImageLoader) {
    Image(
        painter = rememberAsyncImagePainter(R.drawable.load_animation, imageLoader),
        contentDescription = null
    )
}