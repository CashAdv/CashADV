package app.cashadvisor.profile.domain.api

interface InputValidationInteractor {

    suspend fun validateName(name: String): InputValidationState

    suspend fun validateSurname(surname: String): InputValidationState
}