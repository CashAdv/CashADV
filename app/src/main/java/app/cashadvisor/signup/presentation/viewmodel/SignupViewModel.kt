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
import app.cashadvisor.authorization.presentation.ui.test.TestSideEffect
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

//       viewModelScope.launch {
//            registerInteractor.isRegisterInProgress().collect { isInProgress ->
//                _state.update {
//                    it.copy(
//                        isRegisterInProgress = isInProgress
//                    )
//                }
//            }
//        }
        viewModelScope.launch {
            val result = registerInteractor.registerByEmail(
                currentState.email,
                currentState.password
            )

            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Message register ${result.data.message}")
                    sendConfirmationCodeByEmail()
//                    viewModelScope.launch {
//                        _sideEffects.emit(SignupSideEffect.ShowMessage(message = result.data.message))
//                    }
                    _signupScreenState.emit(SignupScreenState.ConfirmationCodeScreen())
                }

                is Resource.Error -> {
//                    viewModelScope.launch {
//                        _sideEffects.emit(SignupSideEffect.ShowMessage(message = "Error: ${result.error.message}"))
//                    }

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

    fun sendRegisterConfirmCode(code: String) {
        viewModelScope.launch {
            logDebugMessage("email: ${currentState.email.value}, code: ${code}")

            val result = registerInteractor.confirmEmailAndRegistrationWithCode(
                currentState.email, ConfirmCode(code)
            )
            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Success register: ${result.data}")
//                    viewModelScope.launch {
//                        _sideEffects.emit(TestSideEffect.ShowMessage(message = result.data))
//                    }

                    viewModelScope.launch {
                        _signupScreenState.emit(SignupScreenState.SignupEmailSuccessfullyConfirmed)
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        //_sideEffects.emit(TestSideEffect.ShowMessage(message = result.error.message))
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
//                                _sideEffects.emit(
//                                    TestSideEffect.ShowMessage(
//                                        "You left only ${result.error.remainingAttempts} attempts \n " +
//                                                "Your lock duration for ${result.error.lockDuration / 1000000000} seconds"
//                                    )
//                                )
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

    companion object{
        private const val VALIDATE_DATA_DELAY_MILLIS = 2000L
        private const val RESENDING_COOL_DOWN = 30000L
        private const val COUNT_DOWN_INTERVAL = 1000L
    }
}