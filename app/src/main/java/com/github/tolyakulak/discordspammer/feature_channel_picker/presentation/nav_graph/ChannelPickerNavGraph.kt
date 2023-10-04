package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel.ChooseChannelScreen
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild.ChooseGuildScreen


fun NavGraphBuilder.channelChooserNavGraph(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = NavigationRoutes.CHANNEL_PICKER_GRAPH,
        startDestination = NavigationRoutes.CHOOSE_GUILD_SCREEN
    ) {
        composable(NavigationRoutes.CHOOSE_GUILD_SCREEN) {
            ChooseGuildScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(NavigationRoutes.CHOOSE_CHANNEL_SCREEN) {
            ChooseChannelScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }

}