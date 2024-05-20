package app.cashadvisor.profile.domain.api

sealed interface InputValidationState {

    data object Default: InputValidationState
    data object Success: InputValidationState

    data class Error(val error: InputValidationError): InputValidationState

}

enum class InputValidationError {
    EMPTY_VALUE,
    INCORRECT_FORMAT
}