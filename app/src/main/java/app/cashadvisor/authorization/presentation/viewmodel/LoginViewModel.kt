package app.cashadvisor.authorization.presentation.viewmodel

import android.content.Context
import android.text.Editable
import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.presentation.ui.models.LoginScreenMessageContent
import app.cashadvisor.authorization.presentation.ui.models.LoginScreenSideEffects
import app.cashadvisor.authorization.presentation.viewmodel.models.LoginScreenState
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import app.cashadvisor.uikit.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val inputValidationInteractor: InputValidationInteractor,
    private val loginInteractor: LoginInteractor
) : BaseViewModel() {

    private var emailState: EmailValidationState = EmailValidationState.Default
    private var passwordState: PasswordValidationState = PasswordValidationState.Default

    private var emailInput = ""
    private var passwordInput = ""

    private var attemptsToSendConfirmationCode = 3

    private var resendCountDownJob: Job? = null

    private val _loginScreenState = MutableStateFlow<LoginScreenState>(
        LoginScreenState.CredentialsInput(
            emailState = emailState,
            passwordState = passwordState,
            isBtnLoginEnabled = false
        )
    )
    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()

    private val _messageEvent = MutableSharedFlow<LoginScreenMessageContent>()
    val messageEvent = _messageEvent.asSharedFlow()

    private val _sideEffects = MutableSharedFlow<LoginScreenSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()


    fun validateCredentials(
        context: Context,
        emailInput: Editable?,
        passwordInput: Editable?
    ) {
        showLoginLoading()
        viewModelScope.launch(Dispatchers.IO) {
            emailState = inputValidationInteractor.validateEmail(emailInput.toString())
            passwordState = inputValidationInteractor.validatePassword(passwordInput.toString())

            if (emailState is EmailValidationState.Error || passwordState is PasswordValidationState.Error) {
                _loginScreenState.value = LoginScreenState.CredentialsInput(
                    emailState = this@LoginViewModel.emailState,
                    passwordState = this@LoginViewModel.passwordState,
                    isBtnLoginEnabled = true
                )

                emitValidationErrorMessage(
                    this@LoginViewModel.emailState,
                    this@LoginViewModel.passwordState
                )
            } else {
                this@LoginViewModel.emailInput = emailInput.toString()
                this@LoginViewModel.passwordInput = passwordInput.toString()

                login(context)
            }
        }

    }

    private fun login(context: Context) {
        showLoginLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val result = loginInteractor.loginByEmail(
                email = Email(emailInput),
                password = Password(passwordInput)
            )

            when (result) {
                is Resource.Error -> {
                    emailState = EmailValidationState.Default
                    passwordState = PasswordValidationState.Default
                    _loginScreenState.value = LoginScreenState.CredentialsInput(
                        isLoginSuccessful = false,
                        isBtnLoginEnabled = true
                    )

                    when (result.error) {
                        is ErrorEntity.Login.FailedToGenerateTokenOrSendEmail -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_failed_to_generate_token_or_send_email,
                                    result.error.message
                                )
                            )

                        }

                        is ErrorEntity.Login.InvalidEmailOrPassword -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_invalid_email_or_password,
                                    result.error.message
                                )
                            )

                            _messageEvent.emit(LoginScreenMessageContent.LoginError(message = result.error.message))
                        }

                        is ErrorEntity.Login.InvalidInput -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_invalid_input,
                                    result.error.message
                                )
                            )
                            _messageEvent.emit(LoginScreenMessageContent.LoginError(message = result.error.message))
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_no_internet,
                                    result.error.message
                                )
                            )
                            _sideEffects.emit(LoginScreenSideEffects.NoInternetConnection)
                        }

                        else -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_something_went_wrong,
                                    result.error.message
                                )
                            )
                            _messageEvent.emit(LoginScreenMessageContent.LoginError(message = result.error.message))
                        }
                    }
                }

                is Resource.Success -> {
                    logDebugMessage(
                        context.getString(
                            R.string.debug_message_message_login,
                            result.data.message
                        )
                    )
                    _loginScreenState.value = LoginScreenState.ConfirmationCode()
                    sendConfirmationCodeByEmail()
                    _sideEffects.emit(LoginScreenSideEffects.HideKeyboard)
                }
            }
        }
    }

    fun credentialsInputListener(
        emailInput: Editable? = null,
        passwordInput: Editable? = null
    ) {
        if (emailInput.toString() == this@LoginViewModel.emailInput) return
        if (passwordInput.toString() == this@LoginViewModel.passwordInput) return
        viewModelScope.launch {
            emailInput?.let {
                emailState = EmailValidationState.Default
                this@LoginViewModel.emailInput = it.toString()
            }
            passwordInput?.let {
                passwordState = PasswordValidationState.Default
                this@LoginViewModel.passwordInput = it.toString()
            }

            val isBtnLoginEnabled =
                this@LoginViewModel.emailInput.isNotBlank() && this@LoginViewModel.passwordInput.isNotBlank()

            _loginScreenState.value = LoginScreenState.CredentialsInput(
                emailState = this@LoginViewModel.emailState,
                passwordState = this@LoginViewModel.passwordState,
                isBtnLoginEnabled = isBtnLoginEnabled
            )
        }
    }

    fun sendConfirmationCodeByEmail() {
        //add some method in future to send code to email
        startCountDownToResendCode()
    }

    private fun startCountDownToResendCode(
    ) {
        resendCountDownJob = viewModelScope.launch(Dispatchers.IO) {
            var allTime = RESENDING_COOL_DOWN
            val interval = COUNT_DOWN_INTERVAL

            while (allTime > 0) {
                _loginScreenState.value = LoginScreenState.ConfirmationCode(
                    resendingCoolDownSec = (allTime / 1000).toString()
                )
                allTime -= interval
                delay(interval)
            }
            _loginScreenState.value = LoginScreenState.ConfirmationCode()
        }
    }

    private fun emitValidationErrorMessage(
        emailValidationState: EmailValidationState,
        passwordValidationState: PasswordValidationState
    ) {
        viewModelScope.launch {
            if (emailValidationState is EmailValidationState.Error && passwordValidationState is PasswordValidationState.Error) {
                when (passwordValidationState.passwordValidationError) {
                    PasswordValidationError.PASSWORD_NOT_VALID -> {
                        _messageEvent.emit(LoginScreenMessageContent.EmailAndPasswordFormatErrors)
                    }

                    PasswordValidationError.PASSWORD_IS_NOT_LONG_ENOUGH -> {
                        _messageEvent.emit(LoginScreenMessageContent.EmailFormatAndPasswordCountErrors)
                    }
                }
            } else if (passwordValidationState is PasswordValidationState.Error) {
                when (passwordValidationState.passwordValidationError) {
                    PasswordValidationError.PASSWORD_NOT_VALID -> {
                        _messageEvent.emit(LoginScreenMessageContent.PasswordFormatError)
                    }

                    PasswordValidationError.PASSWORD_IS_NOT_LONG_ENOUGH -> {
                        _messageEvent.emit(LoginScreenMessageContent.PasswordCountError)
                    }
                }
            } else {
                _messageEvent.emit(LoginScreenMessageContent.EmailFormatError)
            }
        }
    }

    fun navigateBackToCredentialsState() {
        viewModelScope.launch {
            resendCountDownJob?.cancel()
            emailState = EmailValidationState.Default
            passwordState = PasswordValidationState.Default
            _loginScreenState.value = LoginScreenState.CredentialsInput(
                emailState = emailState,
                passwordState = passwordState,
                isLoginSuccessful = null,
                isBtnLoginEnabled = true
            )
        }
    }

    private fun showLoginLoading(){
        viewModelScope.launch {
            _loginScreenState.value = LoginScreenState.CredentialsInput(
                emailState = EmailValidationState.Default,
                passwordState = PasswordValidationState.Default,
                isLoginSuccessful = null,
                isBtnLoginEnabled = true,
                isLoading = true
            )
        }
    }

    fun confirmLoginUsingCode(context: Context, confirmationCode: Editable?) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = loginInteractor.confirmLoginByEmailWithCode(
                Email(emailInput),
                ConfirmCode(confirmationCode.toString())
            )

            when (result) {
                is Resource.Success -> {
                    logDebugMessage(
                        context.getString(
                            R.string.debug_message_success_login,
                            result.data
                        )
                    )
                    viewModelScope.launch {
                        _sideEffects.emit(LoginScreenSideEffects.LoginSuccessfullyConfirmed)
                    }
                }

                is Resource.Error -> {

                    when (result.error) {
                        is ErrorEntity.LoginConfirmationWithCode.InvalidToken -> {

                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_invalid_request_payload,
                                    result.error.message
                                )
                            )
                            _messageEvent.emit(
                                LoginScreenMessageContent.ConfirmationCodeMessage(
                                    context.getString(R.string.invalid_token)
                                )
                            )
                            navigateBackToCredentialsState()
                        }

                        is ErrorEntity.LoginConfirmationWithCode.WrongConfirmationCode -> {

                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_wrong_code,
                                    result.error.message
                                )
                            )
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_wrong_code_remaining_attempts,
                                    result.error.remainingAttempts
                                )
                            )
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_wrong_code_lock_duration,
                                    (result.error.lockDuration / DURATION_CONVERTING_CONST)
                                )
                            )

                            viewModelScope.launch {
                                attemptsToSendConfirmationCode = result.error.remainingAttempts
                                val minutesLeft =
                                    result.error.lockDuration / DURATION_CONVERTING_CONST

                                if (result.error.lockDuration > 0) {
                                    /*_messageEvent.emit(
                                        LoginScreenMessageContent.ConfirmationCodeMessage(
                                            context.getString(
                                                R.string.wrong_code_number_lock_duration,
                                                getRightEndingMinutes(minutesLeft.toInt())
                                            )
                                        )
                                    )*/

                                    _sideEffects.emit(LoginScreenSideEffects.FailedToConfirmLogin(getRightEndingMinutes(minutesLeft.toInt())))

                                } else {
                                    _messageEvent.emit(
                                        LoginScreenMessageContent.ConfirmationCodeMessage(
                                            context.getString(
                                                R.string.wrong_code_number_attempts,
                                                getRightEndingAttempts(
                                                    attemptsToSendConfirmationCode
                                                )
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_no_internet,
                                    result.error.message
                                )
                            )
                            _sideEffects.emit(LoginScreenSideEffects.NoInternetConnection)
                        }

                        else -> {
                            logDebugMessage(
                                context.getString(
                                    R.string.debug_message_something_went_wrong,
                                    result.error.message
                                )

                            )
                            _messageEvent.emit(
                                LoginScreenMessageContent.ConfirmationCodeMessage(
                                    result.error.message
                                )
                            )
                            navigateBackToCredentialsState()
                        }

                    }

                }
            }
        }
    }

    private fun getRightEndingMinutes(minutes: Int): String {
        val preLastDigit = minutes % 100 / 10

        if (preLastDigit == 1) {
            return "$minutes минут"
        }

        return when (minutes % 10) {
            1 -> "$minutes минута"
            2 -> "$minutes минуты"
            3 -> "$minutes минуты"
            4 -> "$minutes минуты"
            else -> "$minutes минут"
        }
    }

    private fun getRightEndingAttempts(attempts: Int): String {
        val preLastDigit = attempts % 100 / 10

        if (preLastDigit == 1) {
            return "$attempts попыток"
        }

        return when (attempts % 10) {
            1 -> "$attempts попытка"
            2 -> "$attempts попытки"
            3 -> "$attempts попытки"
            4 -> "$attempts попытки"
            else -> "$attempts попыток"
        }
    }

    companion object {
        const val RESENDING_COOL_DOWN = 30000L
        const val COUNT_DOWN_INTERVAL = 1000L
        const val DURATION_CONVERTING_CONST = 60000000000
    }

}