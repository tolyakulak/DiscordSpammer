package com.github.tolyakulak.discordspammer.core.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.nav_graph.channelChooserNavGraph
import com.github.tolyakulak.discordspammer.feature_login.presentation.loginNavGraph
import com.github.tolyakulak.discordspammer.feature_spam_service.presentation.SpamProgressScreen
import com.github.tolyakulak.discordspammer.feature_spam_settings.presentation.spam_settings.SpamSettingsScreen

@Composable
fun MyNavHost(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN_GRAPH,
        route = NavigationRoutes.NAV_HOST
    ) {
        loginNavGraph(
            navController = navController, sharedViewModel = sharedViewModel
        )
        channelChooserNavGraph(
            navController = navController, sharedViewModel = sharedViewModel
        )
        composable(NavigationRoutes.SPAM_SETTINGS_SCREEN) {
            SpamSettingsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(NavigationRoutes.SPAM_INFO_SCREEN) {
            SpamProgressScreen(navController = navController)
        }
    }
}