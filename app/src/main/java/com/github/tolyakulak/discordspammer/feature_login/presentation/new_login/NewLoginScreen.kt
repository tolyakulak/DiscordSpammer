package com.github.tolyakulak.discordspammer.feature_login.presentation.new_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModel
import com.github.tolyakulak.discordspammer.core.presentation.shared_view_model.SharedViewModelEvent
import com.github.tolyakulak.discordspammer.core.presentation.util.NavigationRoutes
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLoginScreen(
    navController: NavController, sharedViewModel: SharedViewModel
) {
    val viewModel: NewLoginViewModel = hiltViewModel()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is NewLoginViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message, withDismissAction = true
                    )
                }

                is NewLoginViewModel.UiEvent.SuccessfullyLoggedIn -> {
                    sharedViewModel.onEvent(SharedViewModelEvent.LoginAs(it.token))
                    navController.navigate(NavigationRoutes.CHANNEL_PICKER_GRAPH) {
                        popUpTo(NavigationRoutes.LOGIN_GRAPH) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = context.getString(R.string.add_account)
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, null)
            }
        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = state.token, onValueChange = { newValue ->
                viewModel.onEvent(NewLoginEvent.TokenChanged(newValue))
            }, label = {
                Text(context.getString(R.string.token_label))
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(checked = state.rememberMe, onCheckedChange = {
                    viewModel.onEvent(NewLoginEvent.RememberMeToggled)
                })
                Spacer(Modifier.width(8.dp))
                Text(context.getString(R.string.remember_me))
            }
            Button(onClick = {
                viewModel.onEvent(NewLoginEvent.Submit)
            }) {
                Text(context.getString(R.string.submit))
            }
        }
    }
}