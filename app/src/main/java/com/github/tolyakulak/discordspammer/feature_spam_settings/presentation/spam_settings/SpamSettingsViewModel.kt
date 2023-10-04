package com.github.tolyakulak.discordspammer.feature_spam_settings.presentation.spam_settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpamSettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = savedStateHandle.getStateFlow("spam_settings_state", SpamSettingsState())

    fun onEvent(event: SpamSettingsEvent) {
        when(event) {
            is SpamSettingsEvent.SpamSizeFieldChanged -> {
                savedStateHandle["spam_settings_state"] = state.value.copy(
                    spamSize = event.newValue
                )
            }
        }
    }
}