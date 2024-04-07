package app.cashadvisor.authorization.domain.models.states

import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError

sealed interface PasswordValidationState {
    data class Success(val password: Password) : PasswordValidationState
    data class Error(
        val password: Password,
        val passwordValidationError: PasswordValidationError
    ) : PasswordValidationState
    data object Default : PasswordValidationState
}