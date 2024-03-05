package app.cashadvisor.authorization.domain.models.states

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.ConfirmCodeValidationError

sealed interface ConfirmCodeValidationState {
    data class Success(val confirmCode: ConfirmCode) : ConfirmCodeValidationState
    data class Error(
        val confirmCode: ConfirmCode,
        val codeError: ConfirmCodeValidationError
    ) : ConfirmCodeValidationState
}