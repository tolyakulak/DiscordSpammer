package com.github.tolyakulak.discordspammer.core.presentation.shared_view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import com.github.tolyakulak.discordspammer.core.domain.use_case.coil.CoilUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    // UI layer will access Coil Use Cases using this View Model
    val coilUseCases: CoilUseCases,
    val imageLoader: ImageLoader,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = savedStateHandle.getStateFlow("channel_picker_state", SharedVIewModelState())

    /**
     * Send an event to this View Model
     * @param event any child of [SharedViewModelEvent]
     */
    fun onEvent(event: SharedViewModelEvent) {
        when (event) {
            is SharedViewModelEvent.SelectChannel -> {
                savedStateHandle["channel_picker_state"] = state.value.copy(
                    selectedChannel = event.channel
                )
            }

            is SharedViewModelEvent.SelectGuild -> {
                savedStateHandle["channel_picker_state"] = state.value.copy(
                    selectedGuild = event.guild
                )
            }

            is SharedViewModelEvent.LoginAs -> {
                savedStateHandle["channel_picker_state"] = state.value.copy(token = event.token)
            }
        }
    }
}