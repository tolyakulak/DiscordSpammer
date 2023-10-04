package com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModelEvent
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login.components.LoginLazyItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistLoginScreen(
    navController: NavController, sharedViewModel: SharedViewModel
) {
    val viewModel = hiltViewModel<ExistLoginViewModel>()
    val state by viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ExistLoginViewModel.UiEvent.LoadedAccountsList -> {
                    if (state.listOfTokens.isEmpty()) {
                        navController.navigate(NavigationRoutes.NEW_LOGIN_SCREEN)
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = context.getString(
                    if (state.listOfTokens.isNotEmpty()) {
                        R.string.login_as
                    } else {
                        R.string.empty_accounts
                    }
                )
            )
        })
    }, bottomBar = {
        BottomAppBar {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    navController.navigate(NavigationRoutes.NEW_LOGIN_SCREEN)
                }) {
                    Text(context.getString(R.string.add_account))
                }
            }
        }
    }) { pad ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = pad.calculateTopPadding(), bottom = pad.calculateBottomPadding()
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.listOfTokens) {
                LoginLazyItem(token = it, onClick = {
                    sharedViewModel.onEvent(SharedViewModelEvent.LoginAs(it.token))

                    navController.navigate(NavigationRoutes.CHANNEL_PICKER_GRAPH) {
                        popUpTo(NavigationRoutes.LOGIN_GRAPH) {
                            inclusive = true
                        }
                    }
                }, onDelete = {
                    viewModel.onEvent(ExistLoginEvent.DeleteToken(it))
                })
            }
        }
    }
}