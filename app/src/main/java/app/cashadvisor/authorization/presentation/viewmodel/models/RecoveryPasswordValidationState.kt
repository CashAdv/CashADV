package app.cashadvisor.authorization.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.Password
import app.cashadvisor.authorization.domain.models.PasswordValidationError

interface RecoveryPasswordValidationState {
    data class Success(val password: Password) : RecoveryPasswordValidationState
    data class Error(
        //val password: Password,
        val passwordValidationError: PasswordValidationError
    ) : RecoveryPasswordValidationState
    data object Default : RecoveryPasswordValidationState
}