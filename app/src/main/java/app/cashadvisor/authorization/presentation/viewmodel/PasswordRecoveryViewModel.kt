package app.cashadvisor.authorization.presentation.viewmodel

import android.content.Context
import android.text.Editable
import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.ResetPasswordInteractor
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError
import app.cashadvisor.authorization.domain.models.states.ConfirmCodeValidationState
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordRecoveryScreenState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.presentation.ui.models.RecoveryScreenMessageContent
import app.cashadvisor.authorization.presentation.ui.models.RecoverySideEffect
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryEmailValidationState
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryPasswordValidationState
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val resetPasswordInteractor: ResetPasswordInteractor,
    private val inputValidationInteractor: InputValidationInteractor
) : BaseViewModel() {

    private var emailInput = ""
    private var confirmCode = ""
    private var passwordInput = ""

    private var attemptsToSendConfirmationCode = 3

    private var resendCountDownJob: Job? = null

    private val _uiState: MutableStateFlow<PasswordRecoveryScreenState> = MutableStateFlow(
        PasswordRecoveryScreenState.EmailInput(
            emailState = RecoveryEmailValidationState.Default,
            isBtnLoginEnabled = false
        )
    )
    val uiState: StateFlow<PasswordRecoveryScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<RecoverySideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<RecoverySideEffect> = _sideEffects.asSharedFlow()

    private val _messageEvent = MutableSharedFlow<RecoveryScreenMessageContent>()
    val messageEvent = _messageEvent.asSharedFlow()



     fun setEmail(
        email: String) {
        viewModelScope.launch {
            val result = inputValidationInteractor.validateEmail(email)
            when (result) {
                is EmailValidationState.Success -> {
                    _uiState.value = PasswordRecoveryScreenState.EmailInput(
                        emailState = RecoveryEmailValidationState.Success(result.email),
                        isBtnLoginEnabled = true
                    )
                    emailInput = email
                    recovery()


                }

                is EmailValidationState.Error -> {
                    _uiState.value = PasswordRecoveryScreenState.EmailInput(
                        emailState = RecoveryEmailValidationState.Error(result.email, result.emailValidationError),
                        isBtnLoginEnabled = false
                    )
                    emailValidationErrorMessage(result)
                }
            }
        }
    }

private fun recovery() {

    viewModelScope.launch {
        resetPasswordInteractor.isResetPasswordInProgress().collect{
            _uiState.value
        }
    }

    viewModelScope.launch(Dispatchers.IO) {
        logDebugMessage(emailInput)
        val result = resetPasswordInteractor.confirmEmailForPasswordReset(
            Email(emailInput)
        )
        when (result) {
            is Resource.Success -> {
                logDebugMessage("Message recovery ${result.data.message}")
                viewModelScope.launch {
                    _uiState.value = PasswordRecoveryScreenState.ConfirmationCode()
                    _sideEffects.emit(RecoverySideEffect.HideKeyboard)
                   sendConfirmationCodeByEmail()
                }
            }

            is Resource.Error -> {

                when (result.error) {

                    is ErrorEntity.NetworksError.NoInternet -> {
                        logDebugMessage("NoInternet ${result.error.message}")
                        _sideEffects.emit(RecoverySideEffect.NoInternetConnection)
                    }

                    is ErrorEntity.ConfirmEmailToResetPassword -> {
                        when (result.error) {
                            is ErrorEntity.ConfirmEmailToResetPassword.FailedToGenerateTokenOrSendEmail -> {
                                logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")

                            }

                            is ErrorEntity.ConfirmEmailToResetPassword.InvalidInput -> {
                                logDebugMessage("InvalidEmail ${result.error.message}")
                                _messageEvent.emit(RecoveryScreenMessageContent.LoginError(message = result.error.message))

                            }
                        }
                    }

                    else -> {
                        logDebugMessage("Something went wrong ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.LoginError(message = result.error.message))
                    }
                }
            }
        }
    }

}

fun setEmailConfirmCode(code: String, context: Context) {
    viewModelScope.launch {
        val result = inputValidationInteractor.validateConfirmationCode(code)
        when (result) {
            is ConfirmCodeValidationState.Success -> {
                _uiState.value = PasswordRecoveryScreenState.ConfirmationCode()
                confirmCode = code
                sendEmailConfirmCode(context)
            }

            is ConfirmCodeValidationState.Error -> {



            }
        }
    }
}

private fun sendEmailConfirmCode(context:Context) {
    viewModelScope.launch {
        val result = resetPasswordInteractor.resetPasswordConfirmWithCode(ConfirmCode(confirmCode))
        when (result) {
            is Resource.Success -> {
                viewModelScope.launch {
                    logDebugMessage(result.data)
                    resendCountDownJob!!.cancel()
                   _uiState.value = PasswordRecoveryScreenState.PasswordInput(
                       passwordState = RecoveryPasswordValidationState.Default,
                       isBtnResetPasswordEnabled = false,
                   )
                }
            }

            is Resource.Error -> {

                when (result.error) {
                    is ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput -> {
                        logDebugMessage(
                            context.getString(
                                R.string.debug_message_invalid_request_payload,
                                result.error.message
                            )
                        )
                        _messageEvent.emit(RecoveryScreenMessageContent.ConfirmationCodeMessage(message =
                        context.getString(R.string.invalid_or_expired_token)))
                        navigateBackToEmailState()
                    }

                    is ErrorEntity.ConfirmResetPasswordByEmailWithCode.WrongConfirmationCode -> {

                        logDebugMessage("InvalidToken ${result.error.message}")
                        viewModelScope.launch {
                            val remainingAttempts = result.error.remainingAttempts
                            if(remainingAttempts!=null){
                                attemptsToSendConfirmationCode = remainingAttempts
                            }else{
                                attemptsToSendConfirmationCode -= 1
                            }
                            if(attemptsToSendConfirmationCode>0){
                                _messageEvent.emit(RecoveryScreenMessageContent.ConfirmationCodeMessage(message =
                                context.getString(R.string.invalid_confirmation_code)))
                            }else{
                                _messageEvent.emit(RecoveryScreenMessageContent.ConfirmationCodeMessage(message =
                                context.getString(R.string.user_is_locked)))
                                attemptsToSendConfirmationCode = 3

                                navigateBackToEmailState()
                            }


                        }

                    }

                    is ErrorEntity.ConfirmResetPasswordByEmailWithCode.FailedToConfirmPasswordReset -> {
                        logDebugMessage("WrongConfirmationCode ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.ConfirmationCodeMessage(message =
                        context.getString(R.string.failed_to_confirm_email_or_register_user)))
                        navigateBackToEmailState()
                    }

                    is ErrorEntity.NetworksError.NoInternet -> {
                        logDebugMessage("NoInternet ${result.error.message}")
                        _sideEffects.emit(RecoverySideEffect.NoInternetConnection)

                    }

                    else -> {
                        logDebugMessage(
                            context.getString(
                                R.string.debug_message_something_went_wrong,
                                result.error.message
                            ))
                        _messageEvent.emit(RecoveryScreenMessageContent.ConfirmationCodeMessage(message =
                        result.error.message))
                    }
                }
            }

        }
    }
}

fun setPassword(password: String, context: Context) {
    viewModelScope.launch {
        val result = inputValidationInteractor.validatePassword(password)
        when (result) {
            is PasswordValidationState.Success -> {
                _uiState.value = PasswordRecoveryScreenState.PasswordInput(
                    passwordState = RecoveryPasswordValidationState.Success(result.password),
                    isBtnResetPasswordEnabled = true
                )
                sendNewPassword(context)
            }

            is PasswordValidationState.Error -> {
                _uiState.value = PasswordRecoveryScreenState.PasswordInput(
                    passwordState = RecoveryPasswordValidationState.Error(result.passwordValidationError),
                    isBtnResetPasswordEnabled = false
                )
                passwordValidationErrorMessage(result)
            }
        }
    }
}

private fun sendNewPassword(context: Context) {
    viewModelScope.launch {
        val result =
            resetPasswordInteractor.saveNewPassword(email = Email(emailInput), password = Password(passwordInput))
        when (result) {
            is Resource.Success -> {
                viewModelScope.launch {
                    logDebugMessage(
                        context.getString(R.string.debug_message_success_reset_password)
                    )
                    _sideEffects.emit(RecoverySideEffect.PasswordSuccessfullyConfirmed)
                }
            }

            is Resource.Error -> {
                when (result.error) {
                    is ErrorEntity.SaveNewPassword.InvalidInput -> {
                        logDebugMessage("InvalidInput ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.ResetPasswordError(result.error.message))
                    }

                    is ErrorEntity.SaveNewPassword.InvalidToken -> {
                        logDebugMessage("InvalidToken ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.ResetPasswordError(result.error.message))
                    }

                    is ErrorEntity.SaveNewPassword.FailedToResetPassword -> {
                        logDebugMessage("FailedToResetPassword ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.ResetPasswordError(result.error.message))
                    }

                    is ErrorEntity.NetworksError.NoInternet -> {
                        logDebugMessage("NoInternet ${result.error.message}")
                        _sideEffects.emit(RecoverySideEffect.NoInternetConnection)
                    }

                    else -> {
                        logDebugMessage("Something went wrong ${result.error.message}")
                        _messageEvent.emit(RecoveryScreenMessageContent.ResetPasswordError(result.error.message))
                    }

                }
            }
        }
    }
}
    private fun emailValidationErrorMessage(
        emailValidationState: EmailValidationState,
    ) {
        viewModelScope.launch {
            if (emailValidationState is EmailValidationState.Error) {
                _messageEvent.emit(RecoveryScreenMessageContent.EmailFormatError)

            }
        }
    }
    private fun passwordValidationErrorMessage(
        passwordValidationState: PasswordValidationState
    ){
        viewModelScope.launch{
            if (passwordValidationState is PasswordValidationState.Error){
                when(passwordValidationState.passwordValidationError){
                    PasswordValidationError.PASSWORD_NOT_VALID -> {
                        _messageEvent.emit(RecoveryScreenMessageContent.PasswordFormatError)
                    }
                    PasswordValidationError.PASSWORD_IS_NOT_LONG_ENOUGH -> {
                        _messageEvent.emit(RecoveryScreenMessageContent.PasswordCountError)
                    }
                }
            }
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
                _uiState.value = PasswordRecoveryScreenState.ConfirmationCode(
                    resendingCoolDownSec = (allTime / 1000).toString()
                )
                allTime -= interval
                delay(interval)
            }
            _uiState.value = PasswordRecoveryScreenState.ConfirmationCode()
        }
    }
    fun navigateBackToEmailState() {
        viewModelScope.launch {
            resendCountDownJob?.cancel()
            _uiState.value = PasswordRecoveryScreenState.EmailInput(
                emailState = RecoveryEmailValidationState.Default,
                isLoginSuccessful = null,
                isBtnLoginEnabled = true
            )
        }
    }
    fun emailInputListener(
        emailInput: Editable? = null,

    ) {
        if (emailInput.toString() == this.emailInput) return

        viewModelScope.launch {
            emailInput?.let {
                this@PasswordRecoveryViewModel.emailInput = it.toString()
            }

            val isBtnLoginEnabled =
                this@PasswordRecoveryViewModel.emailInput.isNotBlank()

            _uiState.value = PasswordRecoveryScreenState.EmailInput(
                emailState = RecoveryEmailValidationState.Default,
                isBtnLoginEnabled = isBtnLoginEnabled
            )
        }
    }
    fun passwordInputListener(
        passwordInput:Editable? = null
    ){
       if(passwordInput.toString() == this.passwordInput) return

       viewModelScope.launch{
           passwordInput?.let {
               this@PasswordRecoveryViewModel.passwordInput = passwordInput.toString()
           }
           val isBtnResetPasswordEnabled =
               this@PasswordRecoveryViewModel.passwordInput.isNotBlank()

           _uiState.value = PasswordRecoveryScreenState.PasswordInput(
               passwordState = RecoveryPasswordValidationState.Default,
               isBtnResetPasswordEnabled = isBtnResetPasswordEnabled
           )

       }
    }
    companion object {
        const val RESENDING_COOL_DOWN = 30000L
        const val COUNT_DOWN_INTERVAL = 1000L
    }
}