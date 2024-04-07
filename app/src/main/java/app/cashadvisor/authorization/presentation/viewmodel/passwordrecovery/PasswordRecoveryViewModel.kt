package app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.ResetPasswordInteractor
import app.cashadvisor.authorization.domain.models.states.ConfirmCodeValidationState
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.models.PasswordRecoveryState
import app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.models.PasswordRecoveryUiState
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val resetPasswordInteractor: ResetPasswordInteractor,
    private val inputValidationInteractor: InputValidationInteractor
) : BaseViewModel() {

    private val _uiState:MutableStateFlow<PasswordRecoveryUiState> = MutableStateFlow(
        PasswordRecoveryUiState()
    )
    val uiState:StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

    private val _state: MutableStateFlow<PasswordRecoveryState> = MutableStateFlow(
        PasswordRecoveryState()
    )
    private val state: StateFlow<PasswordRecoveryState> = _state.asStateFlow()

    private val _sideEffects: MutableSharedFlow<RecoverySideEffect> = MutableSharedFlow()

    val sideEffect: SharedFlow<RecoverySideEffect> = _sideEffects.asSharedFlow()
    private val currentState get() = state.replayCache.firstOrNull() ?: PasswordRecoveryState()

    fun init(){
        viewModelScope.launch {
            state.collect{ state ->
                _uiState.update { uiState ->
                    uiState.copy(
                        emailIsValid = state.isEmailValid,
                        confirmEmailCodeIsValid = state.isEmailCodeValid && state.isRecoveryPasswordInProgress,
                        newPasswordIsValid = state.isNewPasswordValid && state.isRecoveryPasswordInProgress
                    )
                }


            }
        }
    }
    fun handleEvent(event: RecoveryPasswordScreenEvent){
        when(event){
            is RecoveryPasswordScreenEvent.SetEmail -> setEmail(event.email)
            RecoveryPasswordScreenEvent.Recovery -> recovery()
            is RecoveryPasswordScreenEvent.SetEmailConfirmCode -> setEmailConfirmCode(event.code)
            RecoveryPasswordScreenEvent.ConfirmEmail -> sendEmailConfirmCode()
            is RecoveryPasswordScreenEvent.SetPassword -> setPassword(event.password)
            RecoveryPasswordScreenEvent.ConfirmNewPassword -> sendNewPassword()
        }
    }
    private fun setEmail(email:String){
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
            }
        }
    }
    private fun recovery(){
        viewModelScope.launch{
            resetPasswordInteractor.isResetPasswordInProgress().collect{ isInProgress ->
                _state.update {
                    it.copy(
                        isRecoveryPasswordInProgress = isInProgress
                    )
                }

            }
        }
        viewModelScope.launch{
            val result = resetPasswordInteractor.confirmEmailForPasswordReset(
                currentState.email
            )
            when(result){
                is Resource.Success -> {
                    logDebugMessage("Message recovery ${result.data.message}")
                    viewModelScope.launch {
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = result.data.message))
                    }
                }
                is Resource.Error -> {
                    viewModelScope.launch{
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = "Error: ${result.error.message}"))
                    }
                    when (result.error) {

                        is ErrorEntity.NetworksError.NoInternet -> {
                            logDebugMessage("NoInternet ${result.error.message}")
                        }

                        is ErrorEntity.ConfirmEmailToResetPassword -> {
                            when (result.error) {
                                is ErrorEntity.ConfirmEmailToResetPassword.FailedToGenerateTokenOrSendEmail -> {
                                    logDebugMessage("FailedToGenerateTokenOrSendEmail ${result.error.message}")
                                }

                                is ErrorEntity.ConfirmEmailToResetPassword.InvalidInput -> {
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
    private fun setEmailConfirmCode(code:String){
        viewModelScope.launch {
            val result = inputValidationInteractor.validateConfirmationCode(code)
            when(result){
                is ConfirmCodeValidationState.Success -> {
                    _state.update {
                        it.copy(emailCode =  result.confirmCode, isEmailCodeValid = true)
                    }
                }
                is ConfirmCodeValidationState.Error -> {
                    _state.update {
                        it.copy(emailCode = result.confirmCode, isEmailCodeValid = false)
                    }
                }
            }
        }
    }
    private fun sendEmailConfirmCode(){
        viewModelScope.launch {
            val result = resetPasswordInteractor.resetPasswordConfirmWithCode(currentState.emailCode)
            when(result){
                is Resource.Success -> {
                    viewModelScope.launch{
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = result.data))
                    }
                }
                is Resource.Error -> {
                    viewModelScope.launch{
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = result.error.message))
                    }
                    when(result.error){
                        is ErrorEntity.ConfirmResetPasswordByEmailWithCode.InvalidInput -> {
                            logDebugMessage("FailedToConfirmEmailOrRegisterUser ${result.error.message}")
                        }
                        is ErrorEntity.ConfirmResetPasswordByEmailWithCode.WrongConfirmationCode -> {

                            logDebugMessage("InvalidToken ${result.error.message}")
                        }
                        is ErrorEntity.ConfirmResetPasswordByEmailWithCode.FailedToConfirmPasswordReset ->{
                            logDebugMessage("WrongConfirmationCode ${result.error.message}")

                        }
                        is ErrorEntity.NetworksError.NoInternet ->{
                            logDebugMessage("NoInternet ${result.error.message}")

                        }
                        else -> {}
                    }
                }

            }
        }
    }
    private fun setPassword(password:String){
        viewModelScope.launch{
            val result = inputValidationInteractor.validatePassword(password)
            when(result){
                is PasswordValidationState.Success -> {
                    _state.update {
                        it.copy(password = result.password, isNewPasswordValid = true)
                    }
                }
                is PasswordValidationState.Error -> {
                    _state.update {
                        it.copy(password = result.password, isNewPasswordValid = false)
                    }
                }
            }
        }
    }
    private fun sendNewPassword(){
        viewModelScope.launch {
            val result = resetPasswordInteractor.saveNewPassword(currentState.email, currentState.password)
            when(result){
                is Resource.Success -> {
                    viewModelScope.launch{
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = result.data.message))
                    }
                }
                is Resource.Error -> {
                    viewModelScope.launch{
                        _sideEffects.emit(RecoverySideEffect.ShowMessage(message = result.error.message))
                    }
                    when(result.error){
                        is ErrorEntity.SaveNewPassword.InvalidInput ->{
                            logDebugMessage("InvalidInput ${result.error.message}")
                        }
                        is ErrorEntity.SaveNewPassword.InvalidToken -> {
                            logDebugMessage("InvalidToken ${result.error.message}")
                        }
                        is ErrorEntity.SaveNewPassword.FailedToResetPassword -> {
                            logDebugMessage("FailedToResetPassword ${result.error.message}")
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
}