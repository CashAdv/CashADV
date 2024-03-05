package app.cashadvisor.authorization.domain.api

import app.cashadvisor.authorization.domain.models.states.ConfirmCodeValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.domain.models.states.EmailValidationState

interface InputValidationInteractor {
    suspend fun validateEmail(email: String): EmailValidationState
    suspend fun validatePassword(password:String): PasswordValidationState
    suspend fun validateConfirmationCode(code:String): ConfirmCodeValidationState

}