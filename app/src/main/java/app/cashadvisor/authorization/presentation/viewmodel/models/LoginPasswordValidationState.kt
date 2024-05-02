package app.cashadvisor.authorization.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError

sealed interface LoginPasswordValidationState {
    data class Success(val password: Password) : LoginPasswordValidationState
    data class Error(
        //val password: Password,
        val passwordValidationError: PasswordValidationError
    ) : LoginPasswordValidationState
    data object Default : LoginPasswordValidationState
}