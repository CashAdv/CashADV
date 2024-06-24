package app.cashadvisor.authorization.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.EmailValidationError

interface RecoveryEmailValidationState {
    data class Success(val email: Email) : RecoveryEmailValidationState
    data class Error(
        val email: Email,
        val emailValidationError: EmailValidationError
    ) : RecoveryEmailValidationState
    data object Default: RecoveryEmailValidationState
}