package com.github.tolyakulak.discordspammer.feature_spam_settings.presentation.spam_settings

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceExtras
import com.github.tolyakulak.discordspammer.core.framework.util.ServiceIntents
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import com.github.tolyakulak.discordspammer.feature_spam_service.framework.SpamService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpamSettingsScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    var spamSizeSupportingTextIsVisible by remember { mutableStateOf(false) }
    var spamSizeIsError by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<SpamSettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(context.getString(R.string.spam_settings_title))
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, null) // TODO add localized content description
            }
        }, actions = {
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.LOGIN_GRAPH) {
                    popUpTo(NavigationRoutes.NAV_HOST) {
                        inclusive = false
                    }
                }
            }) {
                Icon(Icons.Default.Logout, context.getString(R.string.logout))
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = state.spamSize, onValueChange = {
                if (it.isDigitsOnly() || it.isBlank()) {
                    spamSizeSupportingTextIsVisible = false
                    spamSizeIsError = false
                    viewModel.onEvent(SpamSettingsEvent.SpamSizeFieldChanged(it))
                } else {
                    spamSizeSupportingTextIsVisible = true
                    spamSizeIsError = true
                }
            }, label = {
                Text(context.getString(R.string.spam_size_label))
            }, supportingText = {
                AnimatedVisibility(visible = spamSizeSupportingTextIsVisible) {
                    Text(context.getString(R.string.spam_size_supporting))
                }
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ), isError = spamSizeIsError
            )
            Button(onClick = {
                // if clicked again previous task is canceled inside SpamService class

                val spamSize: Int = state.spamSize.toIntOrNull() ?: return@Button

                val intent = Intent(context, SpamService::class.java).apply {
                    action = ServiceIntents.START
                    val state1 = sharedViewModel.state.value
                    putExtra(ServiceExtras.TOKEN, state1.token ?: return@Button)
                    putExtra(ServiceExtras.CHANNEL, state1.selectedChannel?.id ?: return@Button)
                    putExtra(ServiceExtras.AMOUNT, spamSize)
                }
                context.startService(intent)
                navController.navigate(NavigationRoutes.SPAM_INFO_SCREEN)
            }) {
                Text(context.getString(R.string.submit))
            }
        }
    }
}