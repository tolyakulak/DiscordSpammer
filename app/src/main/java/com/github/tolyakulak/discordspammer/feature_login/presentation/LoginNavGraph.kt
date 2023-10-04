package com.github.tolyakulak.discordspammer.feature_login.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login.ExistLoginScreen
import com.github.tolyakulak.discordspammer.feature_login.presentation.new_login.NewLoginScreen

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        startDestination = NavigationRoutes.EXIST_LOGIN_SCREEN,
        route = NavigationRoutes.LOGIN_GRAPH
    ) {
        composable(NavigationRoutes.EXIST_LOGIN_SCREEN) {
            ExistLoginScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(NavigationRoutes.NEW_LOGIN_SCREEN) {
            NewLoginScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}