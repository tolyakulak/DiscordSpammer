package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChooseGuildViewModel @Inject constructor(
    private val discordUseCases: DiscordUseCases,
    private val appContext: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        /** For logging */
        const val TAG = "ChooseGuildViewModel"
    }

    val state = savedStateHandle.getStateFlow("choose_guild_state", ChooseGuildState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ChooseGuildEvent) {
        when (event) {
            is ChooseGuildEvent.UpdateGuilds -> {
                viewModelScope.launch(Dispatchers.IO) {
                    loadGuilds(event.token)
                }
            }
        }
    }

    /**
     * Load new list of guilds into [state] (refresh)
     * @param token authorization token of current user session
     */
    private suspend fun loadGuilds(token: String) {
        savedStateHandle["choose_guild_state"] = state.value.copy(isLoading = true)

        val response = discordUseCases.getMyGuilds(token)
        val list = response.getOrNull()
        val exc = response.exceptionOrNull()

        if (list != null) {
            savedStateHandle["choose_guild_state"] = state.value.copy(
                guildsList = list,
                isLoading = false
            )
        } else {
            Log.e(TAG, "loadGuilds: ", exc)
            when (exc) {
                is HttpException -> {
                    when (exc.code()) {
                        401 -> {
                            showSnackBar(R.string.invalid_token)
                            _eventFlow.emit(UiEvent.Logout)
                        }
                        else -> {
                            showSnackBar(R.string.fail_to_load_list_of_guilds)
                        }
                    }
                }
                is IOException -> {
                    showSnackBar(R.string.network_error)
                }
            }
            savedStateHandle["choose_guild_state"] = state.value.copy(
                isLoading = false
            )
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

    sealed class UiEvent {
        /** Show snack bar */
        data class ShowSnackBar(val message: String) : UiEvent()

        /**
         * An event describing you have to log out.
         *
         * Useful if token is expired
         */
        data object Logout : UiEvent()
    }
}