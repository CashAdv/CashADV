package app.cashadvisor.signup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.RegisterInteractor
import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import app.cashadvisor.signup.presentation.viewmodel.models.SignupDataState
import app.cashadvisor.signup.presentation.viewmodel.models.SignupScreenState
import app.cashadvisor.signup.presentation.viewmodel.models.SignupSideEffect
import app.cashadvisor.signup.presentation.viewmodel.models.SignupUiState
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val inputValidationInteractor: InputValidationInteractor,
): BaseViewModel() {

    private var validateEmailJob: Job? = null
    private var validatePasswordJob: Job? = null
    private var resendCountDownJob: Job? = null
    private var isClickAllowed = true
    private var attemptsToSendConfirmationCode = 3

    private val _signupDataState: MutableStateFlow<SignupDataState> = MutableStateFlow(SignupDataState())
    private val signupDataState: StateFlow<SignupDataState> =_signupDataState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<SignupSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<SignupSideEffect> = _sideEffects.asSharedFlow()

    private val _signupUiState: MutableSharedFlow<SignupUiState> = MutableSharedFlow()
    val signupUiState: SharedFlow<SignupUiState> = _signupUiState.asSharedFlow()

    private val _signupScreenState: MutableStateFlow<SignupScreenState> = MutableStateFlow(SignupScreenState.SignupScreen)
    val signupScreenState: StateFlow<SignupScreenState> = _signupScreenState.asStateFlow()

    private val currentState get() = signupDataState.replayCache.firstOrNull() ?: SignupDataState()

    fun init(){
        viewModelScope.launch {
            signupDataState.collect{
                if (currentState.isEmailValid and currentState.isPasswordValid and currentState.isConfirmPasswordValid)
                    _signupUiState.emit(SignupUiState.SignupDataIsValid)
            }
        }
    }

    fun validateEmail(email: String){

        validateEmailJob?.cancel()

        if (email.isEmpty()) return

        validateEmailJob = viewModelScope.launch {

            val resultValidationEmail
            = inputValidationInteractor.validateEmail(email)

            when (resultValidationEmail){
                is EmailValidationState.Error -> {
                    delay(VALIDATE_DATA_DELAY_MILLIS)
                    _signupDataState.update {it.copy(isEmailValid = false)}

                    _signupUiState.emit(SignupUiState.EmailNotValid)
                }

                is EmailValidationState.Success -> {
                    _signupDataState.update {it.copy(
                        email = Email(email),
                        isEmailValid = true)}

                    _signupUiState.emit(SignupUiState.EmailValid)
                }
            }
        }
    }

    fun validatePassword(password: String){
        validatePasswordJob?.cancel()

        if (password.isEmpty()) return

        if (password == signupDataState.value.password.value) return

        validatePasswordJob = viewModelScope.launch {
            val resultValidatePassword
            = inputValidationInteractor.validatePassword(password)

            when(resultValidatePassword){
                is PasswordValidationState.Error ->{
                    when (resultValidatePassword.passwordValidationError){

                        PasswordValidationError.PASSWORD_IS_SHORT -> {
                            delay(VALIDATE_DATA_DELAY_MILLIS)
                            _signupDataState.update {it.copy(
                                password = Password(password),
                                isPasswordLengthValid = false)}

                            _signupUiState.emit(SignupUiState.PasswordLengthNotValid)
                        }

                        PasswordValidationError.PASSWORD_NOT_VALID -> {
                            delay(VALIDATE_DATA_DELAY_MILLIS)
                            _signupDataState.update {it.copy(
                                password = Password(password),
                                isPasswordValid = false)}

                            _signupUiState.emit(SignupUiState.PasswordNotValid)
                        }
                    }
                }
                is PasswordValidationState.Success ->{
                    _signupDataState.update {it.copy(
                        password = Password(password),
                        isPasswordValid = true,
                        isPasswordLengthValid = true)}

                    _signupUiState.emit(SignupUiState.PasswordValid)
                }
            }
        }
    }

    fun validateConfirmPassword(confirmPassword: String){

        validatePasswordJob?.cancel()

        if (confirmPassword.isEmpty()) return

        validatePasswordJob = viewModelScope.launch {
            val resultValidateConfirmPassword
            = confirmPassword == signupDataState.value.password.value

            if (resultValidateConfirmPassword){
                _signupDataState.update {it.copy(isConfirmPasswordValid = true) }

                _signupUiState.emit(SignupUiState.ConfirmPasswordValid)
            }

            else{
                delay(VALIDATE_DATA_DELAY_MILLIS)
                _signupDataState.update {
                    it.copy(isConfirmPasswordValid = false) }

                _signupUiState.emit(SignupUiState.ConfirmPasswordNotValid)
            }
        }
    }

    fun register (){
        if (!clickDebounce()) return

        viewModelScope.launch {
            val result = registerInteractor.registerByEmail(
                currentState.email,
                currentState.password
            )

            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Message register ${result.data.message}")
                    sendConfirmationCodeByEmail()

                    _signupScreenState.emit(SignupScreenState.ConfirmationCodeScreen())
                }

                is Resource.Error -> {

                    when (result.error) {

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                            _sideEffects.emit(SignupSideEffect
                                .ShowMessage(app.cashadvisor.uikit.R.string.no_internet))
                        }

                        is ErrorEntity.Register -> {
                            when (result.error) {
                                is ErrorEntity.Register.FailedToGenerateTokenOrSendEmail -> {
                                    logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")
                                }
                                is ErrorEntity.Register.InvalidEmail -> {
                                    logDebugMessage("InvalidEmail ${result.error.message}")
                                    _sideEffects.emit(SignupSideEffect
                                        .ShowMessage(app.cashadvisor.uikit.R.string.email_already_exist))
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

    fun sendRegisterConfirmCode(code: String) {
        viewModelScope.launch {
            logDebugMessage("email: ${currentState.email.value}, code: ${code}")

            val result = registerInteractor.confirmEmailAndRegistrationWithCode(
                currentState.email, ConfirmCode(code)
            )
            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Success register: ${result.data}")


                    viewModelScope.launch {
                        _signupScreenState.emit(SignupScreenState.SignupEmailSuccessfullyConfirmed)
                    }
                }

                is Resource.Error -> {
                    when (result.error) {
                        is ErrorEntity.RegisterConfirmationWithCode.FailedToConfirmEmailOrRegisterUser -> {
                            logDebugMessage("FailedToConfirmEmailOrRegisterUser ${result.error.message}")
                        }

                        is ErrorEntity.RegisterConfirmationWithCode.InvalidToken -> {
                            logDebugMessage("InvalidToken ${result.error.message}")
                            _sideEffects.emit(SignupSideEffect
                                .ShowMessage(app.cashadvisor.uikit.R.string.invalid_token))
                        }

                        is ErrorEntity.RegisterConfirmationWithCode.WrongConfirmationCode -> {
                            logDebugMessage("WrongConfirmationCode ${result.error.message}")

                            viewModelScope.launch {
                                attemptsToSendConfirmationCode = result.error.remainingAttempts
                                val minutesLeft =
                                    result.error.lockDuration / DURATION_CONVERTING_CONST

                                if (result.error.lockDuration > 0) {
                                    _sideEffects.emit(
                                        SignupSideEffect
                                            .ShowСhangeableMessage(
                                                app.cashadvisor.uikit.R.string.wrong_code_number_lock_duration,
                                                getRightEndingMinutes(minutesLeft.toInt())
                                            )
                                    )

                                } else {
                                    _sideEffects.emit(
                                        SignupSideEffect
                                            .ShowСhangeableMessage(
                                                app.cashadvisor.uikit.R.string.wrong_code_number_attempts,
                                                getRightEndingAttempts(attemptsToSendConfirmationCode)
                                            )
                                    )
                                }
                            }
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                            _sideEffects.emit(SignupSideEffect
                                .ShowMessage(app.cashadvisor.uikit.R.string.no_internet))
                        }

                        else -> logDebugMessage("Something went wrong ${result.error.message}")
                    }

                }
            }
        }
    }

    fun sendConfirmationCodeByEmail() {
        //add some method in future to send code to email
        startCountDownToResendCode()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(VALIDATE_DATA_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun startCountDownToResendCode() {
        resendCountDownJob = viewModelScope.launch(Dispatchers.IO) {
            var allTime = RESENDING_COOL_DOWN
            val interval = COUNT_DOWN_INTERVAL

            while (allTime > 0) {
                _signupScreenState.value = SignupScreenState.ConfirmationCodeScreen(
                    resendingCoolDownSec = (allTime / 1000).toString()
                )
                allTime -= interval
                delay(interval)
            }
            _signupScreenState.value = SignupScreenState.ConfirmationCodeScreen()
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

    companion object{
        private const val VALIDATE_DATA_DELAY_MILLIS = 2000L
        private const val RESENDING_COOL_DOWN = 30000L
        private const val COUNT_DOWN_INTERVAL = 1000L
        private const val DURATION_CONVERTING_CONST = 60000000000
    }
}