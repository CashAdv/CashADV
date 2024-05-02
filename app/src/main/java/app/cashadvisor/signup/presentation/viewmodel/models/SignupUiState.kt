package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignupUiState{
    data object EmailNotValid: SignupUiState
    data object EmailValid: SignupUiState
    data object PasswordNotValid: SignupUiState
    data object PasswordLengthNotValid: SignupUiState
    data object PasswordValid: SignupUiState
    data object ConfirmPasswordNotValid: SignupUiState
    data object ConfirmPasswordValid: SignupUiState
    data object SignupDataIsValid: SignupUiState
    data object EmailExist: SignupUiState
}

