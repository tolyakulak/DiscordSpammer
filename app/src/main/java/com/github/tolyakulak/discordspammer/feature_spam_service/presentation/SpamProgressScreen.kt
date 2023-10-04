package com.github.tolyakulak.discordspammer.feature_spam_service.presentation

import android.content.Intent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_spam_service.framework.SpamService
import kotlin.math.roundToInt

@Composable
fun SpamProgressScreen(navController: NavController) {
    val progress by SpamService.progress.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val startAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val sweepAngle by animateFloatAsState(
        targetValue = progress * 360,
        label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            drawArc(
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                color = Color.Green,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        Text(
            text = "${(progress * 100).roundToInt()}%"
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(onClick = {
            val intent = Intent(context, SpamService::class.java).apply {
                action = ServiceIntents.STOP
            }
            context.startService(intent)
            navController.navigate(NavigationRoutes.CHANNEL_PICKER_GRAPH) {
                popUpTo(NavigationRoutes.NAV_HOST)
            }
        }) {
            Text(
                text = if(progress == -1f || progress == 1f) {
                    context.getString(R.string.done)
                } else {
                    context.getString(R.string.cancel)
                }
            )
        }
    }
}