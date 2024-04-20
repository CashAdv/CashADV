package app.cashadvisor.authorization.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState

sealed interface LoginScreenState {
    data class CredentialsInput(
        val emailState: EmailValidationState? = null,
        val passwordState: PasswordValidationState? = null,
        val isLoginSuccessful: Boolean? = null,
        val isBtnLoginEnabled: Boolean,
        val isLoading: Boolean? = null
    ) : LoginScreenState

    data class ConfirmationCode(val resendingCoolDownSec: String? = null) : LoginScreenState
}