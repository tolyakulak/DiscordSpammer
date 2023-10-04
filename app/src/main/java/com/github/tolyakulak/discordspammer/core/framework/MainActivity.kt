package com.github.tolyakulak.discordspammer.core.framework

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents
import com.github.tolyakulak.discordspammer.core.presentation.MyNavHost
import com.github.tolyakulak.discordspammer.core.presentation.theme.DiscordSpammerTheme
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }

        setContent {
            DiscordSpammerTheme {
                Surface {
                    navController = rememberNavController()
                    MyNavHost(navController!!)
                    navigateToSpamInfoIfNeeded()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToSpamInfoIfNeeded(intent)
    }

    private fun navigateToSpamInfoIfNeeded(intent: Intent? = null) {
        if (navController == null) {
            Log.d("MainActivity", "navController is null")
            return
        }

        if (this.intent.action == ServiceIntents.OPEN_SPAM_PROGRESS_SCREEN ||
            intent?.action == ServiceIntents.OPEN_SPAM_PROGRESS_SCREEN &&
            navController?.currentDestination?.route != NavigationRoutes.SPAM_INFO_SCREEN
        ) {
            navController?.navigate(NavigationRoutes.SPAM_INFO_SCREEN)
        }
    }
}