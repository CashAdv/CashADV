package app.cashadvisor.authorization.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.EmailValidationError

sealed interface LoginEmailValidationState {
    data class Success(val email: Email) : LoginEmailValidationState
    data class Error(
        val email: Email,
        val emailValidationError: EmailValidationError
    ) : LoginEmailValidationState
    data object Default: LoginEmailValidationState
}