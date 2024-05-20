package app.cashadvisor.profile.domain.impl

import app.cashadvisor.profile.domain.api.InputValidationError
import app.cashadvisor.profile.domain.api.InputValidationInteractor
import app.cashadvisor.profile.domain.api.InputValidationState
import javax.inject.Inject

class InputValidationInteractorImpl @Inject constructor() : InputValidationInteractor {

    override suspend fun validateName(name: String): InputValidationState {
        return when {
            name.isBlank() -> InputValidationState.Error(InputValidationError.EMPTY_VALUE)
            name.matches(Regex(REGEX)) -> InputValidationState.Success
            else -> InputValidationState.Error(InputValidationError.INCORRECT_FORMAT)
        }
    }

    override suspend fun validateSurname(surname: String): InputValidationState {
        return when {
            surname.isBlank() -> InputValidationState.Default
            surname.matches(Regex(REGEX)) -> InputValidationState.Success
            else -> InputValidationState.Error(InputValidationError.INCORRECT_FORMAT)
        }
    }

    companion object {
        private const val REGEX = "^[a-zA-Zа-яА-Я]{1,50}$"
    }
}