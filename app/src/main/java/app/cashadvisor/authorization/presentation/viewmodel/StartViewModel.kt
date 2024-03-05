package app.cashadvisor.authorization.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.AccountInformationInteractor
import app.cashadvisor.authorization.presentation.ui.models.StartScreenUiState
import app.cashadvisor.authorization.presentation.ui.models.StartScreenEvents
import app.cashadvisor.authorization.presentation.ui.models.StartScreenSideEffects
import app.cashadvisor.common.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import app.cashadvisor.authorization.domain.models.states.AccountInformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val accountInformationInteractor: AccountInformationInteractor
) : BaseViewModel() {

    private var isClickAllowed = true

    private val _uiState: MutableStateFlow<StartScreenUiState> =
        MutableStateFlow(StartScreenUiState(StartScreenUiState.UiState.Success()))

    val uiState: StateFlow<StartScreenUiState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<StartScreenSideEffects> = MutableSharedFlow()

    val sideEffects: SharedFlow<StartScreenSideEffects> = _sideEffects.asSharedFlow()

    init {
        setDebugStatus(app.cashadvisor.BuildConfig.DEBUG)
    }

    fun handleEvent(event: StartScreenEvents) {
        when (event) {
            StartScreenEvents.ProceedNextClicked -> {
                if (isClickDebounce()) {
                    loadAuthenticationData()
                }
            }

            StartScreenEvents.UiKitClicked -> {
                if (isClickDebounce()) {
                    viewModelScope.launch {
                        _sideEffects.emit(
                            StartScreenSideEffects.NavigateToUiKitScreen
                        )
                    }
                }
            }
        }
    }

    private fun setDebugStatus(isDebug: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                state = StartScreenUiState.UiState.Success(isDebug = isDebug)
            )
        }
    }

    private fun loadAuthenticationData() {
        viewModelScope.launch {
            try {
                val result = accountInformationInteractor.getAccountInformation().firstOrNull()
                when (result) {
                    is AccountInformation.Authorized -> {
                        _sideEffects.emit(
                            StartScreenSideEffects.NavigateToPinCodeScreen
                        )
                    }

                    else -> {
                        _sideEffects.emit(
                            StartScreenSideEffects.NavigateToAuthenticationScreen
                        )
                    }
                }
            } catch (e: Exception) {
                _sideEffects.emit(
                    StartScreenSideEffects.ShowToast(
                        app.cashadvisor.uikit.R.string.something_went_wrong
                    )
                )
            }
        }
    }

    private fun isClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    }

}