package com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_channel

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tolyakulak.discordspammer.R
import com.github.tolyakulak.discordspammer.core.domain.model.Guild
import com.github.tolyakulak.discordspammer.core.domain.use_case.discord_api.DiscordUseCases
import com.github.tolyakulak.discordspammer.feature_channel_picker.presentation.choose_guild.ChooseGuildViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChooseChannelViewModel @Inject constructor(
    private val discordUseCases: DiscordUseCases,
    private val appContext: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = savedStateHandle.getStateFlow("choose_channel_state", ChooseChannelState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var job: Job? = null

    fun onEvent(event: ChooseChannelEvent) {
        when(event) {
            is ChooseChannelEvent.UpdateChannels -> {
                if(job == null || job?.isActive == false){
                    job = viewModelScope.launch(Dispatchers.IO) {
                        loadChannels(event.token, event.guild)
                    }
                }
            }
        }
    }

    /**
     * Load channels into [state]
     *
     * Changes `state.isLoading`
     * @param token authorization token of current user session
     * @param guild guild you get channels from
     */
    private suspend fun loadChannels(token: String, guild: Guild) {
        savedStateHandle["choose_channel_state"] = state.value.copy(isLoading = true)

        val response = discordUseCases.getChannelsOfGuild(
            token = token,
            guild = guild
        )
        val list = response.getOrNull()
        val exc = response.exceptionOrNull()

        if(list != null) {
            savedStateHandle["choose_channel_state"] = state.value.copy(
                channelsList = list,
                isLoading = false
            )
        } else {
            Log.e(ChooseGuildViewModel.TAG, "loadGuilds: ", exc)
            when (exc) {
                is HttpException -> {
                    when (exc.code()) {
                        401 -> {
                            showSnackBar(R.string.invalid_token)
                            _eventFlow.emit(UiEvent.Logout)
                        }
                        else -> {
                            showSnackBar(R.string.fail_to_load_list_of_channels)
                        }
                    }
                }
                is IOException -> {
                    showSnackBar(R.string.network_error)
                }
            }
            savedStateHandle["choose_channel_state"] = state.value.copy(
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
        data class ShowSnackBar(val message: String) : UiEvent()
        data object Logout: UiEvent()
    }
}