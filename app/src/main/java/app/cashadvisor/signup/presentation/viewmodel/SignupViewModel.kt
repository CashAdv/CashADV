package app.cashadvisor.signup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.R
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
import app.cashadvisor.common.utils.debounce
import app.cashadvisor.signup.presentation.viewmodel.models.SignUpStep
import app.cashadvisor.signup.presentation.viewmodel.models.SignupDataState
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
    
    private var resendCountDownJob: Job? = null
    private var isClickAllowed = true
    private var attemptsToSendConfirmationCode: Int = 3

    private val _signupDataState: MutableStateFlow<SignupDataState> = MutableStateFlow(SignupDataState())
    private val signupDataState: StateFlow<SignupDataState> =_signupDataState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<SignupSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<SignupSideEffect> = _sideEffects.asSharedFlow()

    private val _signupUiState: MutableSharedFlow<SignupUiState> = MutableSharedFlow()
    val signupUiState: SharedFlow<SignupUiState> = _signupUiState.asSharedFlow()

    private val _signUpStep: MutableStateFlow<SignUpStep> = MutableStateFlow(SignUpStep.SignupScreen)
    val signUpStep: StateFlow<SignUpStep> = _signUpStep.asStateFlow()

    private val currentState get() = signupDataState.replayCache.firstOrNull() ?: SignupDataState()


    private lateinit var validationEmailDebounce: (SignupUiState) -> Unit
    private lateinit var validationPasswordDebounce: (SignupUiState) -> Unit
    private lateinit var validationConfirmPasswordDebounce: (SignupUiState) -> Unit

    fun init(){
        viewModelScope.launch {
            signupDataState.collect{
                if (currentState.isEmailValid
                    and currentState.isPasswordValid
                    and currentState.isConfirmPasswordValid)
                    _signupUiState.emit(SignupUiState.SignupDataIsValid)
            }
        }

        validationEmailDebounce = debounce(
            VALIDATE_DATA_DELAY_MILLIS,
            viewModelScope,
            useLastParam = true,
            actionWithDelay = true
        ){ signUpUiState ->
            viewModelScope.launch{
                _signupUiState.emit(value = signUpUiState)
            }
        }

        validationPasswordDebounce = debounce(
            VALIDATE_DATA_DELAY_MILLIS,
            viewModelScope,
            useLastParam = true,
            actionWithDelay = true
        ){ signUpUiState ->
            viewModelScope.launch{
                _signupUiState.emit(value = signUpUiState)
            }
        }

        validationConfirmPasswordDebounce = debounce(
            VALIDATE_DATA_DELAY_MILLIS,
            viewModelScope,
            useLastParam = true,
            actionWithDelay = true
        ){ signUpUiState ->
            viewModelScope.launch{
                _signupUiState.emit(value = signUpUiState)
            }
        }
    }

     fun validateEmail(email: String){

        if (email.isEmpty()){
            _signupDataState.update {it.copy(
                email = Email(email),
                isEmailValid = false)}
            validationEmailDebounce.invoke(SignupUiState.EmailIsEmpty)
            return
        }

         if (email == signupDataState.value.email.value) return

        viewModelScope.launch {

            val resultValidationEmail
            = inputValidationInteractor.validateEmail(email)

            when (resultValidationEmail){
                is EmailValidationState.Error -> {

                    _signupDataState.update {it.copy(
                        email = Email(email),
                        isEmailValid = false)}

                    validationEmailDebounce.invoke(SignupUiState.EmailNotValid)
                }

                is EmailValidationState.Success -> {
                    _signupDataState.update {it.copy(
                        email = Email(email),
                        isEmailValid = true)}

                    validationEmailDebounce.invoke(SignupUiState.EmailValid)
                }
            }
        }
    }

    fun validatePassword(password: String){

        if (password.isEmpty()){
            _signupDataState.update {it.copy(
                password = Password(""),
                isPasswordValid = false)}
            validationPasswordDebounce.invoke(SignupUiState.PasswordIsEmpty)
            return
        }

        if (password == signupDataState.value.password.value) return

        viewModelScope.launch {

            val resultValidatePassword
            = inputValidationInteractor.validatePassword(password)

            when(resultValidatePassword){
                is PasswordValidationState.Error ->{
                    when (resultValidatePassword.passwordValidationError){

                        PasswordValidationError.PASSWORD_IS_NOT_LONG_ENOUGH -> {
                            _signupDataState.update {it.copy(
                                password = Password(password),
                                isPasswordLengthValid = false)}

                            validationPasswordDebounce.invoke(SignupUiState.PasswordLengthNotValid)
                        }

                        PasswordValidationError.PASSWORD_NOT_VALID -> {
                            _signupDataState.update {it.copy(
                                password = Password(password),
                                isPasswordValid = false)}

                            validationPasswordDebounce.invoke(SignupUiState.PasswordNotValid)
                        }
                    }
                }
                is PasswordValidationState.Success ->{
                    _signupDataState.update {it.copy(
                        password = Password(password),
                        isPasswordValid = true,
                        isPasswordLengthValid = true)}

                    validationPasswordDebounce.invoke(SignupUiState.PasswordValid)
                }
            }
                validateConfirmPassword(signupDataState.value.confirmPassword.value)
        }
    }

     fun validateConfirmPassword(confirmPassword: String){

        val resultValidateConfirmPassword
                = confirmPassword == signupDataState.value.password.value

        if (confirmPassword.isEmpty()){
            _signupDataState.update {
                it.copy(confirmPassword = Password(""),
                    isConfirmPasswordValid = false) }
            validationConfirmPasswordDebounce.invoke(SignupUiState.ConfirmPasswordIsEmpty)
            return
        }

        viewModelScope.launch {

            val resultValidateLengthValid = confirmPassword.length >= 8

            if (resultValidateConfirmPassword && resultValidateLengthValid){
                _signupDataState.update {it.copy(
                    confirmPassword = Password(confirmPassword),
                    isConfirmPasswordValid = true) }

                validationConfirmPasswordDebounce.invoke(SignupUiState.ConfirmPasswordValid)
            }

            else if (!resultValidateLengthValid){
                _signupDataState.update {it.copy(
                    confirmPassword = Password(confirmPassword),
                    isConfirmPasswordValid = false) }

                validationConfirmPasswordDebounce.invoke(SignupUiState.ConfirmPasswordLengthNotValid)
            }

            else{
                _signupDataState.update {
                    it.copy(confirmPassword = Password(confirmPassword),
                        isConfirmPasswordValid = false) }

                validationConfirmPasswordDebounce.invoke(SignupUiState.ConfirmPasswordNotValid)
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

                    _signUpStep.emit(SignUpStep.ConfirmationCodeScreen())
                }

                is Resource.Error -> {

                    when (result.error) {

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")

                            _sideEffects.emit(SignupSideEffect.NoInternetConnection)
                        }

                        is ErrorEntity.Register -> {
                            when (result.error) {
                                is ErrorEntity.Register.FailedToGenerateTokenOrSendEmail -> {
                                    logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")
                                }
                                is ErrorEntity.Register.InvalidEmail -> {
                                    logDebugMessage("InvalidEmail ${result.error.message}")

                                    //_signupUiState.emit(SignupUiState.EmailExist)
                                    validationEmailDebounce.invoke(SignupUiState.EmailExist)
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
                        _signUpStep.emit(SignUpStep.SignupEmailSuccessfullyConfirmed)
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
                                    result.error.lockDuration

                                if (result.error.lockDuration > 0) {
                                    _sideEffects.emit(
                                        SignupSideEffect
                                            .ShowChangeableMessage(
                                                app.cashadvisor.uikit.R.string.wrong_code_number_lock_duration,
                                                minutesLeft.toInt(),
                                                R.plurals.ending_minutes
                                            )
                                    )

                                } else {
                                    _sideEffects.emit(
                                        SignupSideEffect
                                            .ShowChangeableMessage(
                                                app.cashadvisor.uikit.R.string.wrong_code_number_attempts,
                                                attemptsToSendConfirmationCode,
                                                R.plurals.ending_attempts
                                            )
                                    )
                                }
                            }
                        }

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                            _sideEffects.emit(SignupSideEffect.NoInternetConnection)
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
                _signUpStep.value = SignUpStep.ConfirmationCodeScreen(
                    resendingCoolDownSec = (allTime / 1000).toString()
                )
                allTime -= interval
                delay(interval)
            }
            _signUpStep.value = SignUpStep.ConfirmationCodeScreen()
        }
    }

    fun navigateBackToCredentialsState() {
        viewModelScope.launch {
            resendCountDownJob?.cancel()
            _signUpStep.value = SignUpStep.SignupScreen
        }
    }

    companion object{
        private const val VALIDATE_DATA_DELAY_MILLIS = 2000L
        private const val RESENDING_COOL_DOWN = 30000L
        private const val COUNT_DOWN_INTERVAL = 1000L
    }
}