package app.cashadvisor.authorization.presentation.ui.test

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.domain.api.RegisterInteractor
import app.cashadvisor.authorization.domain.models.states.ConfirmCodeValidationState
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val loginInteractor: LoginInteractor,
    private val inputValidationInteractor: InputValidationInteractor
) : BaseViewModel() {

    //TODO: отладочная вьюмодель, перед мерджем будет очищена вместе со стейт моделями

    private val _uiState: MutableStateFlow<TestStartUiState> =
        MutableStateFlow(TestStartUiState())

    val uiState: StateFlow<TestStartUiState> = _uiState.asStateFlow()

    private val _state: MutableStateFlow<TestStartState> =
        MutableStateFlow(TestStartState())

    private val state: StateFlow<TestStartState> = _state.asStateFlow()

    private val _sideEffects: MutableSharedFlow<TestSideEffect> = MutableSharedFlow()

    val sideEffect: SharedFlow<TestSideEffect> = _sideEffects.asSharedFlow()

    private val currentState get() = state.replayCache.firstOrNull() ?: TestStartState()

    fun init() {
        viewModelScope.launch {
            state.collect { state ->
                _uiState.update { uiState ->
                    uiState.copy(
                        emailIsValid = state.isEmailValid,
                        registerCodeIsValid = state.isEmailCodeValid && state.isRegisterInProgress,
                        loginCodeIsValid = state.isLoginCodeValid && state.isLoginInProgress
                    )
                }
            }
        }
    }

    fun handleEvent(event: TestScreenEvent) {
        when (event) {
            is TestScreenEvent.SetEmail -> setEmail(event.email)
            TestScreenEvent.Register -> register()
            is TestScreenEvent.SetRegisterConformationCode -> setRegisterConfirmCode(event.code)
            TestScreenEvent.ConfirmRegister -> sendRegisterConfirmCode()
            TestScreenEvent.Login -> login()
            is TestScreenEvent.SetLoginConformationCode -> setLoginConfirmCode(event.code)
            TestScreenEvent.ConfirmLogin -> sendLoginConfirmCode()
        }
    }

    private fun setEmail(email: String) {
        viewModelScope.launch {
            val result = inputValidationInteractor.validateEmail(email)
            when (result) {
                is EmailValidationState.Success -> {
                    _state.update {
                        it.copy(email = result.email, isEmailValid = true)
                    }
                }

                is EmailValidationState.Error -> {
                    _state.update {
                        it.copy(email = result.email, isEmailValid = false)
                    }
                }

                EmailValidationState.Default -> {

                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            registerInteractor.isRegisterInProgress().collect { isInProgress ->
                _state.update {
                    it.copy(
                        isRegisterInProgress = isInProgress
                    )
                }
            }
        }
        viewModelScope.launch {
            val result = registerInteractor.registerByEmail(
                currentState.email,
                currentState.password
            )

            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Message register ${result.data.message}")
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.data.message))
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = "Error: ${result.error.message}"))
                    }

                    when (result.error) {

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                        }

                        is ErrorEntity.Register -> {
                            when (result.error) {
                                is ErrorEntity.Register.FailedToGenerateTokenOrSendEmail -> {
                                    logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")
                                }

                                is ErrorEntity.Register.InvalidEmail -> {
                                    logDebugMessage("InvalidEmail ${result.error.message}")

                                }
                            }
                        }

                        else -> {
                            logDebugMessage("Something went wrong ${result.error.message}")
                        }
                    }
                }

            }
        }
    }

    private fun sendRegisterConfirmCode() {
        viewModelScope.launch {
            logDebugMessage("email: ${currentState.email.value}, code: ${currentState.emailCode.value}")

            val result = registerInteractor.confirmEmailAndRegistrationWithCode(
                currentState.email, currentState.emailCode
            )
            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Success register: ${result.data}")
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.data))
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.error.message))
                    }

                    when (result.error) {
                        is ErrorEntity.RegisterConfirmationWithCode.FailedToConfirmEmailOrRegisterUser -> {
                            logDebugMessage("FailedToConfirmEmailOrRegisterUser ${result.error.message}")
                        }

                        is ErrorEntity.RegisterConfirmationWithCode.InvalidToken -> {
                            logDebugMessage("InvalidToken ${result.error.message}")
                        }

                        is ErrorEntity.RegisterConfirmationWithCode.WrongConfirmationCode -> {
                            logDebugMessage("WrongConfirmationCode ${result.error.message}")
                            viewModelScope.launch {
                                _sideEffects.emit(
                                    TestSideEffect.ShowMessage(
                                        "You left only ${result.error.remainingAttempts} attempts \n " +
                                                "Your lock duration for ${result.error.lockDuration / 1000000000} seconds"
                                    )
                                )
                            }
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                        }

                        else -> logDebugMessage("Something went wrong ${result.error.message}")
                    }

                }
            }
        }
    }

    private fun setRegisterConfirmCode(code: String) {
        viewModelScope.launch {
            val result = inputValidationInteractor.validateConfirmationCode(code)
            when (result) {

                is ConfirmCodeValidationState.Success -> {
                    _state.update {
                        it.copy(emailCode = result.confirmCode, isEmailCodeValid = true)
                    }
                }

                is ConfirmCodeValidationState.Error -> _state.update {
                    it.copy(emailCode = result.confirmCode, isEmailCodeValid = false)
                }
            }
        }
    }


    private fun login() {
        viewModelScope.launch {
            loginInteractor.isLoginInProgress().collect { isInProgress ->
                _state.update {
                    it.copy(
                        isLoginInProgress = isInProgress
                    )
                }
            }
        }

        viewModelScope.launch {
            val result = loginInteractor.loginByEmail(
                currentState.email,
                currentState.password
            )
            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Message login ${result.data.message}")
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.data.message))
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = "Error: ${result.error.message}"))
                    }

                    when (result.error) {
                        is ErrorEntity.Login.FailedToGenerateTokenOrSendEmail -> {
                            logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")
                        }

                        is ErrorEntity.Login.InvalidEmailOrPassword -> {
                            logDebugMessage("InvalidEmailOrPassword ${result.error.message}")
                        }

                        is ErrorEntity.Login.InvalidInput -> {
                            logDebugMessage("InvalidInput ${result.error.message}")
                        }


                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                        }

                        else -> {
                            logDebugMessage("Something went wrong ${result.error.message}")
                        }
                    }
                }
            }
        }
    }

    private fun sendLoginConfirmCode() {
        viewModelScope.launch {
            val result = loginInteractor.confirmLoginByEmailWithCode(
                currentState.email,
                currentState.loginCode,
            )

            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Success login: ${result.data}")
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.data))
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        _sideEffects.emit(TestSideEffect.ShowMessage(message = "Error: ${result.error.message}"))
                    }

                    when (result.error) {


                        is ErrorEntity.LoginConfirmationWithCode.InvalidToken -> {
                            logDebugMessage("InvalidRequestPayload ${result.error.message}")
                        }

                        is ErrorEntity.LoginConfirmationWithCode.WrongConfirmationCode -> {
                            logDebugMessage("WrongConfirmationCode ${result.error.message}")
                            logDebugMessage("WrongConfirmationCode remaining attempts ${result.error.remainingAttempts}")
                            logDebugMessage("WrongConfirmationCode lock duration ${result.error.lockDuration}")

                            viewModelScope.launch {
                                _sideEffects.emit(
                                    TestSideEffect.ShowMessage(
                                        "You left only ${result.error.remainingAttempts} attempts \n " +
                                                "Your lock duration for ${result.error.lockDuration / 1000000000} seconds"
                                    )
                                )
                            }
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                        }

                        else -> {
                            logDebugMessage("Something went wrong ${result.error.message}")
                        }
                    }
                }
            }
        }
    }

    private fun setLoginConfirmCode(code: String) {
        viewModelScope.launch {
            val result = inputValidationInteractor.validateConfirmationCode(code)

            when (result) {
                is ConfirmCodeValidationState.Success -> {
                    _state.update {
                        it.copy(loginCode = result.confirmCode, isLoginCodeValid = true)
                    }
                }

                is ConfirmCodeValidationState.Error -> _state.update {
                    it.copy(loginCode = result.confirmCode, isLoginCodeValid = false)
                }
            }
        }
    }

}