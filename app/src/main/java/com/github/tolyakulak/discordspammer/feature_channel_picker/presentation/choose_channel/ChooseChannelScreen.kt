package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModelEvent
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel.components.ChannelLazyItem
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.common.components.LoadingAnimation
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseChannelScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val viewModel = hiltViewModel<ChooseChannelViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(
            ChooseChannelEvent.UpdateChannels(
                sharedViewModel.state.value.token!!,
                sharedViewModel.state.value.selectedGuild!!
            )
        )

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ChooseChannelViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }

                ChooseChannelViewModel.UiEvent.Logout -> {
                    navController.navigate(NavigationRoutes.LOGIN_GRAPH) {
                        popUpTo(NavigationRoutes.CHANNEL_PICKER_GRAPH) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(context.getString(R.string.choose_channel))
        }, actions = {
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.LOGIN_GRAPH) {
                    popUpTo(NavigationRoutes.NAV_HOST)
                }
            }) {
                Icon(Icons.Default.Logout, null)
            }
            IconButton(onClick = {
                viewModel.onEvent(
                    ChooseChannelEvent.UpdateChannels(
                        sharedViewModel.state.value.token!!,
                        sharedViewModel.state.value.selectedGuild!!
                    )
                )
            }) {
                Icon(Icons.Default.Refresh, null)
            }
        }, navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(Icons.Default.ArrowBack, null)
            }
        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) { paddingValues ->
        if(state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                contentAlignment = Alignment.Center
            ){
                LoadingAnimation(imageLoader = sharedViewModel.imageLoader)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                items(state.channelsList) { channel ->
                    ChannelLazyItem(
                        channel = channel,
                        onClick = {
                            sharedViewModel.onEvent(SharedViewModelEvent.SelectChannel(channel))
                            navController.navigate(NavigationRoutes.SPAM_SETTINGS_SCREEN)
                        }
                    )
                }
            }
        }
    }
}