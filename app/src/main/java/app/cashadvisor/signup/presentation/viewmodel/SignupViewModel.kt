package app.cashadvisor.signup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.domain.api.RegisterInteractor
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.presentation.ui.test.TestSideEffect
import app.cashadvisor.authorization.presentation.ui.test.TestStartState
import app.cashadvisor.authorization.presentation.ui.test.TestStartUiState
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import app.cashadvisor.signup.presentation.viewmodel.models.SignupDataState
import app.cashadvisor.signup.presentation.viewmodel.models.SignupSideEffect
import app.cashadvisor.signup.presentation.viewmodel.models.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val loginInteractor: LoginInteractor,
    private val inputValidationInteractor: InputValidationInteractor,
): BaseViewModel() {

    private var validateEmailJob: Job? = null
    private var validatePasswordJob: Job? = null
    private var isClickAllowed = true

    private val _signupDataState: MutableStateFlow<SignupDataState> = MutableStateFlow(SignupDataState())
    private val signupDataState: StateFlow<SignupDataState> =_signupDataState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<SignupSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<SignupSideEffect> = _sideEffects.asSharedFlow()

    private val _signupUiState: MutableSharedFlow<SignupUiState> = MutableSharedFlow()
    val signupUiState: SharedFlow<SignupUiState> = _signupUiState.asSharedFlow()

    private val currentState get() = signupDataState.replayCache.firstOrNull() ?: SignupDataState()

    fun init(){
        viewModelScope.launch {
            signupDataState.collect(){
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
                    _signupDataState.update {it.copy(isEmailValid = false,)}

                    _signupUiState.emit(SignupUiState.EmailNotValid)
                }

                is EmailValidationState.Success -> {
                    _signupDataState.update {it.copy(isEmailValid = true)}

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

//        viewModelScope.launch {
//            loginInteractor.isLoginInProgress().collect { isInProgress ->
//                _state.update {
//                    it.copy(
//                        isLoginInProgress = isInProgress
//                    )
//                }
//            }
//        }

        viewModelScope.launch {
            val result = loginInteractor.loginByEmail(
                currentState.email,
                currentState.password
            )
            when (result) {
                is Resource.Success -> {
                    logDebugMessage("Message login ${result.data.message}")
                    viewModelScope.launch {
                        _sideEffects.emit(SignupSideEffect.ShowMessage(message = result.data.message))
                    }
                }

                is Resource.Error -> {
                    viewModelScope.launch {
                        _sideEffects.emit(SignupSideEffect.ShowMessage(message = "Error: ${result.error.message}"))
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

    companion object{
        private const val VALIDATE_DATA_DELAY_MILLIS = 2000L
    }
}