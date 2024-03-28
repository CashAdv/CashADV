package app.cashadvisor.authorization.domain.models.states

import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.EmailValidationError

sealed interface EmailValidationState {
    data class Success(val email: Email) : EmailValidationState
    data class Error(
        val email: Email,
        val emailValidationError: EmailValidationError
    ) : EmailValidationState
    data object Default: EmailValidationState
}