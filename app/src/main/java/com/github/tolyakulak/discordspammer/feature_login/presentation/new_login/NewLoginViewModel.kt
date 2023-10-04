package com.github.tolyakulak.discordspammer.feature_login.presentation.new_login

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.domain.model.User
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import com.github.tolyakulak.discordspammer.feature_login.domain.model.Token
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewLoginViewModel @Inject constructor(
    private val appContext: Application,
    private val loginUseCases: LoginUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val discordUseCases: DiscordUseCases
) : ViewModel() {
    companion object {
        const val TAG = "NewLoginViewModel"
    }

    val state = savedStateHandle.getStateFlow("login_state", NewLoginState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var isLoading = false

    /**
     * Main safe.
     *
     * Launches new coroutine without blocking thread
     */
    fun onEvent(event: NewLoginEvent) {
        when (event) {
            NewLoginEvent.Submit -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (isLoading) {
                        showSnackBar(R.string.login_is_busy)
                        return@launch
                    }
                    isLoading = true
                    val token = state.value.token
                    val result = discordUseCases.getMyAccount(token)
                    val me: User? = result.getOrNull()

                    if (result.isSuccess && me != null) {
                        if (state.value.rememberMe) {
                            loginUseCases.upsertToken(
                                Token(
                                    token = token, username = me.username
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.SuccessfullyLoggedIn(token))
                    } else {
                        val err = result.exceptionOrNull()?.also {
                            Log.e(TAG, "onEvent: ", it)
                        }

                        when (err) {
                            is HttpException, is IllegalArgumentException -> {
                                showSnackBar(R.string.invalid_token)
                            }

                            is IOException -> {
                                showSnackBar(R.string.network_error)
                            }
                        }
                    }
                    isLoading = false
                }
            }

            is NewLoginEvent.TokenChanged -> {
                if (!isLoading) {
                    savedStateHandle["login_state"] = state.value.copy(
                        token = event.value
                    )
                }
            }

            NewLoginEvent.RememberMeToggled -> {
                savedStateHandle["login_state"] = state.value.copy(
                    rememberMe = !state.value.rememberMe
                )
            }
        }
    }

    /**
     * Helper function to send [UiEvent.ShowSnackBar] to [_eventFlow]
     *
     * Shows a snack bar with string by resource id
     *
     * @param resId resource id of string to show in snack bar
     */
    private suspend fun showSnackBar(resId: Int) {
        _eventFlow.emit(UiEvent.ShowSnackBar(appContext.getString(resId)))
    }

    /**
     * Describes events sent from ViewModel to UI
     */
    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()

        data class SuccessfullyLoggedIn(val token: String) : UiEvent()
    }
}