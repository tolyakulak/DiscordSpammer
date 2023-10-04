package com.github.tolyakulak.discordspammer.feature_login.presentation.exist_login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tolyakulak.discordspammer.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExistLoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private var _state = mutableStateOf(ExistLoginState())
    val state: State<ExistLoginState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCases.getTokens().collectLatest {
                _state.value = state.value.copy(
                    listOfTokens = it
                )
                _eventFlow.emit(UiEvent.LoadedAccountsList)
            }
        }
    }

    /**
     * Main safe.
     *
     * Launches new coroutine without blocking thread
     */
    fun onEvent(event: ExistLoginEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is ExistLoginEvent.DeleteToken -> {
                    loginUseCases.deleteToken(event.token)
                }
            }
        }
    }

    sealed class UiEvent {
        /**
         * Event describing view model loaded accounts list
         */
        data object LoadedAccountsList : UiEvent()
    }
}